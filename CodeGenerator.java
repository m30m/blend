import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

class Variable {
    enum ADDR_MODE {GLOBAL_DIRECT, GLOBAL_INDIRECT, LOCAL_DIRECT, LOCAL_INDIRECT, IMMEDIATE}

    enum TYPE {INT, REAL, BOOL, STRING, CHAR}


    ADDR_MODE mode;
    TYPE type;
    int value;

    public Variable(ADDR_MODE mode, TYPE type, int value) {
        this.mode = mode;
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        String modeStr = modeToStr(mode);
        String typeStr = typeToStr(type);
        return modeStr + typeStr + value;
    }

    private String typeToStr(TYPE type) {
        switch (type) {

            case INT:
                return "i_";
            case REAL:
                return "f_";
            case BOOL:
                return "b_";
            case STRING:
                return "s_";
            case CHAR:
                return "c_";
        }
        return "ERR";
    }

    private String modeToStr(ADDR_MODE mode) {
        switch (mode) {
            case GLOBAL_DIRECT:
                return "gd_";
            case GLOBAL_INDIRECT:
                return "gi_";
            case LOCAL_DIRECT:
                return "ld_";
            case LOCAL_INDIRECT:
                return "li_";
            case IMMEDIATE:
                return "im_";
        }
        return "ERR";
    }
}


class Function {
    ArrayList<Type> return_types;
    ArrayList<Type> arguments;//FIXME
    HashMap<Identifier, Variable> vars;
    int localVarsSize = 0;
    Identifier id;
    int start_PC;

    public Function() {
        return_types = new ArrayList<>();
        vars = new HashMap<>();
    }

    public Variable addVariable(Type t, Identifier varId) {
        if (vars.containsKey(varId))
            throw new RuntimeException("Duplicate declaration of " + varId.id + " in function " + id.id);


        Variable v = new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, Variable.TYPE.valueOf(t.type.toUpperCase()), localVarsSize);
        vars.put(varId, v);
        localVarsSize += t.getByteSize();
        return v;
    }

    public Variable getVariable(Identifier varId) {
        if (!vars.containsKey(varId))
            return null;
        return vars.get(varId);
    }
}

public class CodeGenerator {
    Function currentFunction;
    Scanner scanner; // This was my way of informing CG about Constant Values detected by Scanner, you can do whatever you like
    HashMap<Identifier, Function> functionsHashMap;
    ArrayList<String> instructions;
    int PC;
    Variable SP;
    Variable SP_place;
    Variable AX;

    Stack<Object> sstack;

    // Define any variables needed for code generation

    private int tmp_addr;
    private Variable lastVariable;

    public CodeGenerator(Scanner scanner) {
        this.scanner = scanner;
        this.sstack = new Stack<>();
        this.instructions = new ArrayList<>();
        this.functionsHashMap = new HashMap<>();
        this.PC = 0;
        this.SP = new Variable(Variable.ADDR_MODE.GLOBAL_DIRECT, Variable.TYPE.INT, 0);
        makeIns(":=", makeConst(1000 * 1000), SP);
        this.SP_place = new Variable(Variable.ADDR_MODE.GLOBAL_INDIRECT, Variable.TYPE.INT, 0);
        this.AX = new Variable(Variable.ADDR_MODE.GLOBAL_DIRECT, Variable.TYPE.INT, 4);
        this.currentFunction = null;
        makeIns("jmp", makeConst(1000));
    }

    private Variable makeConst(int i) {
        return new Variable(Variable.ADDR_MODE.IMMEDIATE, Variable.TYPE.INT, i);
    }

    public void makeIns(String opCode, Variable opr3, Variable opr1, Variable opr2) {
        String ins = opCode + " " + opr1 + " " + opr2 + " " + opr3 + "\n";
        instructions.add(ins);
        PC++;
    }

    public void makeIns(String opCode, Variable opr2, Variable opr1) {
        String ins = opCode + " " + opr1 + " " + opr2 + "\n";
        instructions.add(ins);
        PC++;
    }

    public void makeIns(String opCode, Variable opr1) {
        String ins = opCode + " " + opr1 + "\n";
        instructions.add(ins);
        PC++;
    }

    public void Generate(String sem, Token currentToken) {
        System.out.println(sem); // Just for debug

        switch (sem) {
            case "NoSem":
                return;
            case "push": {
                assert currentToken instanceof Literal;
                Variable v = new Variable(Variable.ADDR_MODE.IMMEDIATE, Variable.TYPE.INT, Integer.parseInt(((Literal) currentToken).value));
                sstack.push(v);
                break;
            }
            case "BIN_DIV":
            case "BIN_MOD":
            case "BIN_MUL":
            case "BIN_SUB":
            case "BIT_AND":
            case "BIT_XOR":
            case "BIT_OR":
            case "BIN_AND":
            case "BIN_OR":
            case "BIN_ADD": {
                //TODO:Check type
                Variable tempInt = getTempInt();
                Variable op1 = (Variable) sstack.pop();
                Variable op2 = (Variable) sstack.pop();
                makeIns(opToString(sem), tempInt, op1, op2);
                sstack.push(tempInt);
                break;
            }
            case "pushType": {
                sstack.push(currentToken);
                break;
            }
            case "pushId": {
                sstack.push(currentToken);
                break;
            }
            case "pushconst"://FIXME make it camel case
            {
                sstack.push(makeConst((Literal) currentToken));
                break;
            }
            case "varDeclare": {
                Type t = (Type) sstack.pop();
                Identifier id = (Identifier) currentToken;
                Variable variable = currentFunction.addVariable(t, id);
                this.lastVariable = variable;
                break;
            }
            case "assign": {
                //assign the pushed id to the
                Variable opr1 = (Variable) sstack.pop();
                Identifier id = (Identifier) sstack.pop();
                Variable opr2 = currentFunction.getVariable(id);
                if (opr2 == null)
                    throw new RuntimeException("Assignment before decleartion of variable " + id.id);
                makeIns(":=", opr1, opr2);
                break;
            }
            case "assignDeclared": {
                makeIns(":=", (Variable) sstack.pop(), lastVariable);
                break;
            }
            case "startFunc": {
                sstack.push("start_function");
                break;
            }
            case "finFunc": {
                Function f = new Function();

                //FIXME handle input arguments
                f.id = (Identifier) sstack.pop();
                if (functionsHashMap.containsKey(f.id))
                    throw new RuntimeException("Duplicate function decleration with name: " + f.id);
                while (!sstack.peek().equals("start_function"))//reading return types
                {
                    f.return_types.add(0, (Type) sstack.pop());
                }
                sstack.pop();
                sstack.push(f);
                functionsHashMap.put(f.id, f);
                currentFunction = f;
                f.start_PC = this.PC + 1;
                break;
            }
            case "finFuncBody": {
                //FIXME
                break;
            }
            case "return":
            {

                break;
            }

            case "UNARY_NOT":
            case "COMPLEMENT":
            case "NEGATION":
                return;
            default: {
                System.out.println("Unknown sem = " + sem);
                throw new RuntimeException("Unknown sem = " + sem);
//                return;
            }

        }
    }

    private Variable makeConst(Literal currentToken) {
        switch (currentToken.type) {
            case "CHAR": {
                throw new RuntimeException("Not Implemeneted");
                //return new Variable(Variable.ADDR_MODE.IMMEDIATE, Variable.TYPE.CHAR,'c');//FIXME
            }
            case "REAL": {
                throw new RuntimeException("Not Implemeneted");
            }
            case "HEX": {
                return new Variable(Variable.ADDR_MODE.IMMEDIATE, Variable.TYPE.INT, Integer.parseInt(currentToken.value, 16));
            }
            case "INT": {
                return new Variable(Variable.ADDR_MODE.IMMEDIATE, Variable.TYPE.INT, Integer.parseInt(currentToken.value));
            }
            case "BOOL": {
                if (currentToken.value.equals("false"))
                    return new Variable(Variable.ADDR_MODE.IMMEDIATE, Variable.TYPE.BOOL, 0);
                else if (currentToken.value.equals("true"))
                    return new Variable(Variable.ADDR_MODE.IMMEDIATE, Variable.TYPE.BOOL, 1);
                else
                    throw new RuntimeException("Boolean with value other than true or false");
            }
        }
        throw new RuntimeException("Unknown type for const: " + currentToken.type);
    }

    private void push(Variable v) {
        makeIns("-", SP, new Variable(Variable.ADDR_MODE.IMMEDIATE, Variable.TYPE.INT, 4), SP);
        makeIns(":=", v, SP_place);
    }

    private void callFunction(Function f, ArrayList<Variable> arguments) {
        if (arguments.size() > 0) {

            //Check argument types and lenght with function
            for (Variable argument : arguments) {
                push(argument);
            }
            throw new NotImplementedException();//FIXME
        }
        makeIns(":=pc", AX);
        makeIns("+", AX, makeConst(8), AX);
        push(AX);
        makeIns("jmp", makeConst(f.start_PC));
    }

    private String opToString(String op) {
        switch (op) {
            case "BIN_DIV":
                return "/";
            case "BIN_MOD":
                return "%";
            case "BIN_MUL":
                return "*";
            case "BIN_SUB":
                return "-";
            case "BIT_AND":
                return "&";
            case "BIT_XOR":
                return "^";
            case "BIT_OR":
                return "|";
            case "BIN_AND":
                return "&&";
            case "BIN_OR":
                return "||";
            case "BIN_ADD":
                return "+";
            case "LESS":
                return "<";
            case "LEQ":
                return "<=";
            case "EQUAL":
                return "==";
            case "MORE":
                return ">";
            case "MEQ":
                return ">=";
            case "NEQ":
                return "!";
        }
        return "ERR";
    }

    private Variable getTempInt() {
        return new Variable(Variable.ADDR_MODE.GLOBAL_DIRECT, Variable.TYPE.INT, getTempAddr());
    }

    private int getTempAddr() {
        int ans = tmp_addr;
        tmp_addr += 4;
        return ans;
    }

    public void FinishCode() // You may need this
    {

    }

    public void WriteOutput(String outputName) {
        // Can be used to print the generated code to output
        // I used this because in the process of compiling, I stored the generated code in a structure
        // If you want, you can output a code line just when it is generated (strongly NOT recommended!!)
        try {
            PrintWriter writer = new PrintWriter(outputName, "UTF-8");
            Identifier id = new Identifier("id", "main");
            if (!functionsHashMap.containsKey(id)) {
                throw new RuntimeException("No function main");
            }

            //Fix jump
            String ins = "jmp " + makeConst(PC) + "\n";
            instructions.set(1, ins);
            callFunction(functionsHashMap.get(new Identifier("id", "main")), new ArrayList<>());

            for (String instruction : instructions) {
                writer.print(instruction);
            }
            writer.close();
        } catch (IOException e) {
            // do something
        }
    }
}
