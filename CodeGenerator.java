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

    public int getByteSize() {
        switch (type) {
            case STRING:
                return 4;
            case INT:
                return 4;
            case REAL:
                return 4;
            case CHAR:
                return 1;
            case BOOL:
                return 1;
//            case "long": // FIXME
//                return 8;
        }
        throw new RuntimeException("Unknown type");
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
    ArrayList<Variable> argsOrder;//FIXME
    HashMap<Identifier, Variable> vars;
    int localVarsSize = 0;
    Identifier id;
    int start_PC;
    Variable return_addr;
    Variable old_base_pointer;

    public Function() {
        return_types = new ArrayList<>();
        vars = new HashMap<>();
        argsOrder = new ArrayList<>();
        old_base_pointer = new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, Variable.TYPE.INT, -4);
        return_addr = new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, Variable.TYPE.INT, -8);

        localVarsSize += 8;//return address and old base pointer
    }

    public void addArgument(Type t, Identifier argId) {
        if (vars.containsKey(argId))
            throw new RuntimeException("Arguments with the same name!");
        localVarsSize += t.getByteSize();
        Variable arg = new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, Variable.TYPE.valueOf(t.type.toUpperCase()), -localVarsSize);
        vars.put(argId, arg);
        argsOrder.add(arg);

    }

    public Variable addVariable(Type t, Identifier varId) {
        if (vars.containsKey(varId))
            throw new RuntimeException("Duplicate declaration of " + varId.id + " in function " + id.id);
        localVarsSize += t.getByteSize();
        Variable v = new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, Variable.TYPE.valueOf(t.type.toUpperCase()), -localVarsSize);
        vars.put(varId, v);
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
    private boolean insideFuncDef;

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

    public void makeIns(String opCode, Variable opr1, Variable opr2, Variable opr3) {
        String ins = opCode + " " + opr1 + " " + opr2 + " " + opr3 + "\n";
        instructions.add(ins);
        PC++;
    }

    public void makeIns(String opCode, Variable opr1, Variable opr2) {
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
                makeIns(opToString(sem), op1, op2, tempInt);
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
                if (insideFuncDef) {
                    currentFunction.addArgument(t, id);
                } else {
                    Variable variable = currentFunction.addVariable(t, id);
                    this.lastVariable = variable;
                }
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
                if (insideFuncDef)
                    throw new RuntimeException("Can't assign variable inside declaration");
                makeIns(":=", (Variable) sstack.pop(), lastVariable);
                break;
            }
            case "startFunc": {
                sstack.push("start_function");
                insideFuncDef = true;
                break;
            }
            case "finFuncSignature": {
                Function f = new Function();


                f.id = (Identifier) sstack.pop();
                if (functionsHashMap.containsKey(f.id))
                    throw new RuntimeException("Duplicate function decleration with name: " + f.id);
                while (!sstack.peek().equals("start_function"))//reading return types
                {
                    f.return_types.add(0, (Type) sstack.pop());
                }
                functionsHashMap.put(f.id, f);
                currentFunction = f;
                f.start_PC = this.PC;
                break;
            }
            case "finFunc": {
                Function f = new Function();
                //FIXME handle input arguments

                sstack.pop(); // pop "start_function"
                sstack.push(f);
                insideFuncDef = false;
                break;
            }
            case "finFuncBody": {
                pop(currentFunction.localVarsSize);
                makeIns("wi", currentFunction.return_addr);
                makeIns("jmp", currentFunction.return_addr);
                currentFunction = null;
                break;
            }
            case "functionCall":
            {
                sstack.push("start_function_args");
                break;
            }
            case "finishFuncArgs": {
                ArrayList<Variable> args = new ArrayList<>();
                while (!sstack.peek().equals("start_function_args"))//reading return types
                {
                    Variable var = (Variable) sstack.pop();
                    args.add(var);
                }
                sstack.pop();//popping start_function_args
                Identifier functionName = (Identifier) sstack.pop();
                if (functionName.id.equals("write")) {
                    if (args.size() != 1)
                        throw new RuntimeException("write function gets only one parameter");
                    Variable writeVar = args.get(0);
                    switch (writeVar.type) {
                        case INT:
                            makeIns("wi", writeVar);
                            break;
                        case REAL:
                            makeIns("wf", writeVar);
                            break;
                        case CHAR:
                            makeIns("wt", writeVar);
                            break;
                        default:
                            throw new RuntimeException("Can't write such variable");
                    }
                    break;
                }
                if (!functionsHashMap.containsKey(functionName))
                    throw new RuntimeException("Function " + functionName.id + " doesn't exist");
                Function function = functionsHashMap.get(functionName);
                callFunction(function, args);
                break;
            }
            case "return": {

                break;
            }

            case "UNARY_NOT":
            case "COMPLEMENT":
            case "NEGATION":
            case "pushArg":
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

    private void pop(int size) {
        makeIns("+", SP, makeConst(size), SP);
    }
    private void push(Variable v) {
        makeIns("-", SP, makeConst(v.getByteSize()), SP);
        makeIns(":=", v, SP_place);
    }

    private void callFunction(Function f, ArrayList<Variable> arguments) {
        makeIns(":=sp", AX);
        makeIns("sp:=", SP);
        push(AX);
        makeIns(":=", makeConst(PC), AX);
        makeIns("+", AX, makeConst(1 + 1 + 2 + 2 * arguments.size() + 1), AX);
        push(AX);
        //Check argument types and length with function
        if (arguments.size() != f.argsOrder.size())
            throw new RuntimeException("Arguments number is not correct");
        for (int i = 0; i < arguments.size(); i++) {
            if (arguments.get(i).type != f.argsOrder.get(i).type)
                throw new RuntimeException("Argument types are not the same");
            push(arguments.get(i));
        }

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
            makeIns(":=", AX, AX);//dummy operation
            for (String instruction : instructions) {
                writer.print(instruction);
            }
            writer.close();
        } catch (IOException e) {
            // do something
        }
    }
}
