import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.atomic.DoubleAccumulator;


public class CodeGenerator {
    Function currentFunction;
    Scanner scanner; // This was my way of informing CG about Constant Values detected by Scanner, you can do whatever you like
    HashMap<Identifier, Function> functionsHashMap;
    ArrayList<String> instructions;
    int PC;
    Variable SP;
    Variable SP_place;
    Variable AX;
    Variable FUNCTION_RESULT;

    Stack<Object> sstack;

    // Define any variables needed for code generation

    private Variable lastVariable;
    private boolean insideFuncDef;
    private boolean isRightHandSide;

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
        this.FUNCTION_RESULT = new Variable(Variable.ADDR_MODE.GLOBAL_DIRECT, Variable.TYPE.INT, 8);
        this.currentFunction = null;
        makeIns("jmp", makeConst(0));//the value of jmp is not important since it will be overwritten in the end
    }

    private Variable makeConst(int i) {
        return new Variable(Variable.ADDR_MODE.IMMEDIATE, Variable.TYPE.INT, i);
    }

    public void makeIns(String opCode, Variable opr1, Variable opr2, Variable opr3) {
        String ins = getIns(opCode, opr1, opr2, opr3);
        instructions.add(ins);
        PC++;
    }

    private String getIns(String opCode, Variable opr1, Variable opr2, Variable opr3) {
        return opCode + " " + opr1 + " " + opr2 + " " + opr3 + "\n";
    }

    public void makeIns(String opCode, Variable opr1, Variable opr2) {
        String ins = getIns(opCode, opr1, opr2);
        instructions.add(ins);
        PC++;
    }

    private String getIns(String opCode, Variable opr1, Variable opr2) {
        return opCode + " " + opr1 + " " + opr2 + "\n";
    }

    public void makeIns(String opCode, Variable opr1) {
        String ins = getIns(opCode, opr1);
        instructions.add(ins);
        PC++;
    }

    private String getIns(String opCode, Variable opr1) {
        return opCode + " " + opr1 + "\n";
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
            case "BOOL_EQ":
            case "BOOL_NEQ":
            case "GREATER":
            case "LESS":
            case "GEQ":
            case "LEQ": {
                Variable tempBool = currentFunction.getTempBool();
                Variable op2 = (Variable) sstack.pop();
                Variable op1 = (Variable) sstack.pop();
                makeIns(opToString(sem), op1, op2, tempBool);
                sstack.push(tempBool);
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
                //TODO:Implement casting
                Variable op2 = (Variable) sstack.pop();
                Variable op1 = (Variable) sstack.pop();
                Variable tempInt = currentFunction.getTemp(op1.type, 1);
                makeIns(opToString(sem), op1, op2, tempInt);
                sstack.push(tempInt);
                break;
            }
            case "pushType": {
                sstack.push(currentToken);
                break;
            }
            case "pushId": {
                if (currentFunction != null && currentFunction.vars.containsKey(currentToken))
                    sstack.push(currentFunction.vars.get(currentToken));
                else
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
            case "setRHS": {
                isRightHandSide = true;
                break;
            }
            case "assign": {
                //assign the pushed id to the
                Variable opr1 = (Variable) sstack.pop();
                Variable opr2;
                if (sstack.peek() instanceof Identifier) {
                    Identifier id = (Identifier) sstack.pop();
                    opr2 = currentFunction.getVariable(id);
                    if (opr2 == null)
                        throw new RuntimeException("Assignment before decleartion of variable " + id.id);
                } else
                    opr2 = (Variable) sstack.pop();

                makeIns(":=", opr1, opr2);
                isRightHandSide = false;
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
                makeIns("-", SP, makeConst(0), SP);
                break;
            }
            case "startDoLoop": {
                Loop l = new Loop();
                l.startAddr = PC;
                currentFunction.loopStack.push(l);
                //fill the loop stack with block start
                //push to loop stack
                System.out.println("sem = " + sem);
                break;
            }
            case "startWhileBlock": {
                //done
                Loop l = currentFunction.loopStack.peek();
                l.startAddr = PC;
                break;
            }
            case "finWhileLoop": {
                Loop l = currentFunction.loopStack.peek();
                makeIns("jmp", makeConst(l.whileAddr));
                break;
            }
            case "startWhileLoop": {
                //done
                currentFunction.loopStack.push(new Loop());
                //push to loop stack
                System.out.println("sem = " + sem);
                break;
            }
            case "finWhileCondition": {
                System.out.println("sem = " + sem);
                Loop l = currentFunction.loopStack.peek();
                Variable var = (Variable) sstack.pop();
                l.conditionVar = var;
                l.jzAddr = PC; // Jump location is not known yet
                instructions.add("");
                PC++;
                l.jmpAddr = PC; // Jump location is not known yet
                instructions.add("");
                PC++;
                break;
            }
            case "startWhileCondition": {
                //done
                Loop l = currentFunction.loopStack.peek();
                l.whileAddr = PC;
                //fill the loop stack check address
                System.out.println("sem = " + sem);
                break;
            }
            case "finLoop": {
                Loop l = currentFunction.loopStack.pop();
                l.endAddr = PC;
                for (Integer breakAddr : l.breakAddrs)
                    instructions.set(breakAddr, getIns("jmp", makeConst(l.endAddr)));
                for (Integer continueAddr : l.continueAddrs)
                    instructions.set(continueAddr, getIns("jmp", makeConst(l.whileAddr)));
                instructions.set(l.jzAddr, getIns("jz", l.conditionVar, makeConst(l.endAddr)));
                instructions.set(l.jmpAddr, getIns("jmp", makeConst(l.startAddr)));
                //fill the loop stack with the next address
                System.out.println("sem = " + sem);
                //pop from the sstack and fill anything necessary
                //pop from loop stack
                break;
            }
            case "break": {
                Loop l = currentFunction.loopStack.peek();
                l.breakAddrs.add(PC);
                instructions.add("");
                PC++;
                break;
            }
            case "continue": {
                Loop l = currentFunction.loopStack.peek();
                l.continueAddrs.add(PC);
                instructions.add("");
                PC++;
                break;
            }

            case "finFunc": {
                Function f = new Function();//FIXME: This is bullshit
                sstack.pop(); // pop "start_function"
                sstack.push(f);
                insideFuncDef = false;
                break;
            }
            case "finFuncBody": {
                end_current_function();
                instructions.set(currentFunction.start_PC, getIns("-", SP, makeConst(currentFunction.localVarsSize), SP));
                currentFunction = null;
                break;
            }
            case "functionCall": {
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
                //FIXME multi return, check return type
                Identifier id = (Identifier) currentToken;
                Variable variable = currentFunction.getVariable(id);
                if (variable == null)
                    throw new RuntimeException("returning unidentified identifier");
                makeIns(":=", variable, FUNCTION_RESULT);
                end_current_function();
                break;
            }

            case "finIfCondition": {
                Variable var = (Variable) sstack.pop();
                sstack.push(PC);
                sstack.push(var);
                sstack.push("start_if");
                makeIns("jz", var, makeConst(0)); // Jump location is not known yet
                break;
            }
            case "finIf": {
                if (!sstack.pop().equals("start_if"))
                    throw new RuntimeException();
                Variable condition = (Variable) sstack.pop();
                Integer PC_pos = (Integer) sstack.pop();
                instructions.set(PC_pos, getIns("jz", condition, makeConst(PC)));
                sstack.push(PC_pos);
                sstack.push(condition);
                sstack.push("end_if");
                break;
            }
            case "startElse": {
                if (!sstack.pop().equals("end_if"))
                    throw new RuntimeException();
                Variable condition = (Variable) sstack.pop();
                instructions.set((Integer) sstack.pop(), getIns("jz", condition, makeConst(PC + 1)));
                sstack.push(PC);
                sstack.push("start_else");
                makeIns("jmp", makeConst(0));
                break;
            }
            case "finElse": {
                if (!sstack.pop().equals("start_else"))
                    throw new RuntimeException();
                instructions.set((Integer) sstack.pop(), getIns("jmp", makeConst(PC)));
                sstack.push(0);
                sstack.push("end_if");
                break;
            }
            case "finCondition": {
                if (!sstack.pop().equals("end_if"))
                    throw new RuntimeException();
                sstack.pop();
                break;
            }
            case "returnStart":
            {
                break;
            }
            case "returnId":
            {
                // check case "return"
                break;
            }
            case "returnFin":

            case "initTuple":
            case "addTuple":
            case "finTuple":

            case "assignStructEnd":// implement the scanner too
            case "assignStructStart":
            case "assignStructEndEmpty":
            case "assignStructEmpty":
            case "structEnd":
            case "structVar":
            case "structId":

            case "envEnd":
            case "envVar":
            case "envId":

            case "labelId":
            {

                break;
            }
            case "gotoId":
            {
                System.out.println("sem = " + sem);
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

    private void end_current_function() {
        pop(currentFunction.localVarsSize);
        makeIns(":=", currentFunction.return_addr, AX);
        makeIns("sp:=", currentFunction.old_base_pointer);
        makeIns("jmp", AX);
    }

    private Variable makeConst(Literal currentToken) {
        switch (currentToken.type) {
            case "CHAR": {
                //Trim the single quotes
                String charValue = currentToken.value.substring(1, currentToken.value.length() - 1);
                return new Variable(Variable.ADDR_MODE.IMMEDIATE, Variable.TYPE.CHAR, charValue);
            }
            case "REAL": {
                return new Variable(Variable.ADDR_MODE.IMMEDIATE, Variable.TYPE.REAL, String.valueOf(Double.parseDouble(currentToken.value)));
            }
            case "HEX": {
                //Trim the leading 0x
                return new Variable(Variable.ADDR_MODE.IMMEDIATE, Variable.TYPE.INT, Integer.parseInt(currentToken.value.substring(2), 16));
            }
            case "INT": {
                return new Variable(Variable.ADDR_MODE.IMMEDIATE, Variable.TYPE.INT, Integer.parseInt(currentToken.value));
            }
            case "BOOL": {
                if (currentToken.value.equals("false"))
                    return new Variable(Variable.ADDR_MODE.IMMEDIATE, Variable.TYPE.BOOL, "false");
                else if (currentToken.value.equals("true"))
                    return new Variable(Variable.ADDR_MODE.IMMEDIATE, Variable.TYPE.BOOL, "true");
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
        //Check argument types and length with function
        if (arguments.size() != f.argsOrder.size())
            throw new RuntimeException("Arguments number is not correct");
        for (int i = arguments.size() - 1; i >= 0; i--) {
            if (arguments.get(i).type != f.argsOrder.get(i).type)
                throw new RuntimeException("Argument types are not the same");
            push(arguments.get(i));
        }
        makeIns(":=sp", AX);
        push(makeConst(0));
        int jump_instruction = PC - 1;
        push(AX);
        makeIns("sp:=", SP);
        makeIns("jmp", makeConst(f.start_PC));
        instructions.set(jump_instruction, getIns(":=", makeConst(PC), SP_place));
        if (currentFunction != null && isRightHandSide) {
            Variable result = currentFunction.getTempInt();
            makeIns(":=", FUNCTION_RESULT, result);
            sstack.push(result);
        }
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
            case "BOOL_EQ":
                return "==";
            case "BOOL_NEQ":
                return "==";
            case "GREATER":
                return ">";
            case "GEQ":
                return ">=";
            case "NEQ":
                return "!";
        }
        return "ERR";
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

    public boolean isStruct(String yytext) {
//        throw new RuntimeException("Not IMplemented");
        return false;//FIXME
    }
}
