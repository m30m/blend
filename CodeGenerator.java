import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

class Case {
    int min_val, max_val;
    int break_jmp_addr;
    int end_addr;
    Variable expr;
    HashMap<Integer, Integer> const_to_addr = new HashMap<>();

    public Case(int break_jmp_addr, Variable expr) {
        this.break_jmp_addr = break_jmp_addr;
        this.expr = expr;
    }

}

class StructType extends Type {
    int size = 0;
    ArrayList<Map.Entry<Identifier, Type>> fields = new ArrayList<>();

    public StructType(String parser_token, String type) {
        super(parser_token, type);
    }

    boolean hasField(Identifier fieldId) {
        for (Map.Entry<Identifier, Type> field : fields) {
            if (field.getKey().equals(fieldId))
                return true;
        }
        return false;
    }

    public void addField(Type t, Identifier fieldId) {
        if (hasField(fieldId))
            throw new RuntimeException("Duplicate declaration of field " + fieldId.id + " in struct");
        size += t.getByteSize();
        fields.add(new AbstractMap.SimpleEntry<Identifier, Type>(fieldId, t));
    }

    public int getFieldOffset(Identifier fieldId) {
        int offset = 0;
        for (Map.Entry<Identifier, Type> field : fields) {
            if (field.getKey().equals(fieldId))
                return offset;
            offset += field.getValue().getByteSize();
        }
        throw new RuntimeException("Field " + fieldId.parser_token + " doesn't exist in struct");
    }

    public Type getFieldType(Identifier fieldId) {
        for (Map.Entry<Identifier, Type> field : fields) {
            if (field.getKey().equals(fieldId))
                return field.getValue();
        }
        throw new RuntimeException("Field " + fieldId.parser_token + " doesn't exist in struct");
    }


    @Override
    public int getTypeSize() {
        return 4;
    }

    @Override
    String typeToVMStr() {
        return "i_";//just a pointer
    }


    public int getStructSize() {
        return size;
    }
}


public class CodeGenerator {
    Function currentFunction;
    StructType currentStruct = null;
    Scanner scanner; // This was my way of informing CG about Constant Values detected by Scanner, you can do whatever you like
    HashMap<Identifier, Function> functionsHashMap;
    HashMap<String, StructType> structsHashMap;
    ArrayList<String> instructions;
    int PC;
    Variable SP;
    Variable SP_place;
    Variable AX;
    Variable TMP_BOOL; // used for switch case
    Variable TMP_CHAR;
    Variable TMP_INT;
    Variable FUNCTION_RESULT;
    static Type intType = new PrimitiveType("type", "int");
    static Type boolType = new PrimitiveType("type", "bool");
    static Type realype = new PrimitiveType("type", "real");
    static Type charType = new PrimitiveType("type", "char");

    Stack<Object> sstack;

    // Define any variables needed for code generation

    private boolean insideFuncDef;
    private boolean insideStructDef = false;
    private boolean insideEnvDef = false;
    private boolean insideStructAssign = false;
    private int tupleSize = 0;

    public CodeGenerator(Scanner scanner) {
        this.scanner = scanner;
        this.sstack = new Stack<>();
        this.instructions = new ArrayList<>();
        this.functionsHashMap = new HashMap<>();
        this.structsHashMap = new HashMap<>();
        this.PC = 0;
        this.SP = new Variable(Variable.ADDR_MODE.GLOBAL_DIRECT, intType, 0);
        makeIns(":=", makeConst(1000 * 1000), SP);
        this.SP_place = new Variable(Variable.ADDR_MODE.GLOBAL_INDIRECT, intType, 0);
        this.AX = new Variable(Variable.ADDR_MODE.GLOBAL_DIRECT, intType, 4);
        this.FUNCTION_RESULT = new Variable(Variable.ADDR_MODE.GLOBAL_DIRECT, intType, 8);
        this.TMP_BOOL = new Variable(Variable.ADDR_MODE.GLOBAL_DIRECT, boolType, 12);
        this.TMP_CHAR = new Variable(Variable.ADDR_MODE.GLOBAL_DIRECT, charType, 13);
        this.TMP_INT = new Variable(Variable.ADDR_MODE.GLOBAL_DIRECT, intType, 14);
        this.currentFunction = null;
        makeIns("jmp", makeConst(0));//the value of jmp is not important since it will be overwritten in the end
    }

    private Variable makeConst(int i) {
        return new Variable(Variable.ADDR_MODE.IMMEDIATE, intType, i);
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

    public boolean isInsideStructAssign() {
        return insideStructAssign;
    }

    public void Generate(String sem, Token currentToken) {
        //System.out.println(sem); // Just for debug

        switch (sem) {
            case "NoSem":
                return;
            case "push": {
                assert currentToken instanceof Literal;
                Variable v = new Variable(Variable.ADDR_MODE.IMMEDIATE, intType, Integer.parseInt(((Literal) currentToken).value));
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
                if (op1.type.type.equals("char") || op2.type.type.equals("char")) {
                    assign(makeConst(0), AX);
                    assign(makeConst(0), TMP_INT);
                    assign(op1, AX);
                    assign(op2, TMP_INT);
                    makeIns(opToString(sem), AX, TMP_INT, TMP_INT);
                    assign(TMP_INT, tempBool);
                } else
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
                Variable tempInt = currentFunction.getTemp(op1.type);
                if (op1.type.type.equals("char") || op2.type.type.equals("char")) {
                    assign(makeConst(0), AX);
                    assign(makeConst(0), TMP_INT);
                    assign(op1, AX);
                    assign(op2, TMP_INT);
                    makeIns(opToString(sem), AX, TMP_INT, TMP_INT);
                    assign(TMP_INT, tempInt);
                } else
                    makeIns(opToString(sem), op1, op2, tempInt);
                sstack.push(tempInt);
                break;
            }
            case "pushType": {
                sstack.push(currentToken);
                break;
            }
            case "pushArrayType": {
                Type t = (Type) currentToken;
                t.isArray = true;
                sstack.push(currentToken);
                break;
            }
            case "pushId": {
                if (currentFunction != null && currentFunction.containsKey((Identifier) currentToken))
                    sstack.push(currentFunction.getVariable((Identifier) currentToken));
                else
                    sstack.push(currentToken);
                break;
            }
            case "namespace": {
                sstack.push((Identifier) currentToken);
                break;
            }
            case "pushid"://Change name to pushIdWithNamespace
            {
                Identifier envId = (Identifier) sstack.pop();
                if (currentFunction != null) {
                    if (currentFunction.containsKey((Identifier) currentToken, envId))
                        sstack.push(currentFunction.getVariable((Identifier) currentToken, envId));
                    else
                        throw new RuntimeException("id in environment doesn't exist");
                } else
                    throw new RuntimeException("Not Implemented");//FIXME
                break;
            }
            case "pushconst": {
                sstack.push(makeConst((Literal) currentToken));
                break;
            }
            case "varDeclare": {
                Type t = (Type) sstack.pop();
                Identifier id = (Identifier) currentToken;
                if (insideStructDef) {
                    currentStruct.addField(t, id);
                } else if (insideFuncDef) {
                    currentFunction.addArgument(t, id);
                } else {
                    Variable variable = currentFunction.addVariable(t, id);
                    sstack.push(variable);
                    if ((t instanceof StructType) || t.isArray) {
                        assign(makeConst(0), variable);
                    }
                }
                break;
            }
            case "setRHS": {//FIXME: push the result anyhow and pop it after the semicolon
                break;
            }
            case "assign": {

                if (sstack.peek() instanceof ArrayList) {
                    ArrayList<Object> tuple1 = (ArrayList<Object>) sstack.pop();
                    ArrayList<Object> tuple2 = (ArrayList<Object>) sstack.pop();
                    if (tuple1.size() != tuple2.size())
                        throw new RuntimeException("Tuple sizes are not equal for assignment");
                    for (int i = 0; i < tuple1.size(); i++) {
                        assign((Variable) tuple1.get(i), tuple2.get(i));
                    }
                    sstack.push(tuple2);
                } else if (!sstack.peek().equals("struct_assign_fin") && !sstack.peek().equals("array_assign_fin")) {//assignment is handled elsewhere
                    Variable opr1 = (Variable) sstack.pop();
                    Object assigned_var = sstack.pop();
                    assign(opr1, assigned_var);
                    sstack.push(assigned_var);
                }
                break;
            }
            case "assignDeclared": {
                if (insideFuncDef || insideStructDef)
                    throw new RuntimeException("Can't assign variable inside declaration");
                if (sstack.peek().equals("struct_assign_fin") || sstack.peek().equals("array_assign_fin")) {//assignment is handled elsewhere
                    break;
                }
                Variable value = (Variable) sstack.pop();
                Variable variable = (Variable) sstack.pop();
                makeIns(":=", value, variable);
                sstack.push(variable);
                break;
            }
            case "startFunc": {
                sstack.push("start_function");
                insideFuncDef = true;
                break;
            }
            case "STMTFin": {
                sstack.pop();
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
                //System.out.println("sem = " + sem);
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
                //System.out.println("sem = " + sem);
                break;
            }
            case "finWhileCondition": {
                //System.out.println("sem = " + sem);
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
                //System.out.println("sem = " + sem);
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
                //System.out.println("sem = " + sem);
                //pop from the sstack and fill anything necessary
                //pop from loop stack
                sstack.push("dummy");
                break;
            }
            case "break": {
                Loop l = currentFunction.loopStack.peek();
                l.breakAddrs.add(PC);
                instructions.add("");
                PC++;
                sstack.push("dummy");
                break;
            }
            case "continue": {
                Loop l = currentFunction.loopStack.peek();
                l.continueAddrs.add(PC);
                instructions.add("");
                PC++;
                sstack.push("dummy");
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
                    if (!(sstack.peek() instanceof Variable))
                        throw new RuntimeException("id not exists");
                    Variable var = (Variable) sstack.pop();
                    args.add(var);
                }
                sstack.pop();//popping start_function_args
                Identifier functionName = (Identifier) sstack.pop();
                if (functionName.id.equals("write")) {
                    if (args.size() != 1)
                        throw new RuntimeException("write function gets only one parameter");
                    Variable writeVar = args.get(0);
                    switch (writeVar.type.type) {
                        case "int":
                            makeIns("wi", writeVar);
                            break;
                        case "real":
                            makeIns("wf", writeVar);
                            break;
                        case "char":
                            makeIns("wt", writeVar);
                            break;
                        case "bool":
                            assign(makeConst(0), AX);
                            assign(writeVar, AX);
                            makeIns("wi", AX);
                            break;
                        default:
                            throw new RuntimeException("Can't write such variable");
                    }
                    sstack.push("dummy");//dummy result of write function
                    break;
                } else if (functionName.id.equals("isvoid")) {
                    if (args.size() != 1)
                        throw new RuntimeException("isvoid function gets only one parameter");
                    Variable writeVar = args.get(0);
                    if (!(writeVar.type instanceof StructType) && !writeVar.type.isArray) // and array
                    {
                        sstack.push(makeConst(new Literal("const", "BOOL", "false")));//result of isvoid
                    }
                    else
                    {
                        Variable tmpBool = currentFunction.getTemp(boolType);
                        makeIns("==",writeVar,makeConst(0),tmpBool);
                        sstack.push(tmpBool);
                    }
                    break;
                }
                else if (functionName.id.equals("read"))
                {
                    if (args.size() != 1)
                        throw new RuntimeException("read function gets only one parameter");
                    Variable readVar = args.get(0);
                    switch (readVar.type.type) {
                        case "int":
                            makeIns("ri", readVar);
                            break;
                        case "real":
                            makeIns("rf", readVar);
                            break;
                        case "char":
                            makeIns("rt", readVar);
                            break;
                        case "bool":
                            makeIns("ri", AX);
                            makeIns("!=", AX, makeConst(0), readVar);
                            break;
                        default:
                            throw new RuntimeException("Can't write such variable");
                    }
                    sstack.push("dummy");//dummy result of read function
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
                throw new RuntimeException("Use parenthesis for return");
//                Identifier id = (Identifier) currentToken;
//                Variable variable = currentFunction.getVariable(id);
//                if (variable == null)
//                    throw new RuntimeException("returning unidentified identifier");
//                makeIns(":=", variable, FUNCTION_RESULT);
//                sstack.push("dummy");//dummy for STMTFin
//                end_current_function();
//                break;
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
                sstack.pop();//PC
                sstack.pop();//Condition
                sstack.push("dummy");
                break;
            }
            case "returnStart": {
                sstack.push("return_start");
                break;
            }
            case "returnId": {
                // check case "return"
                Identifier id = (Identifier) currentToken;
                Variable variable = currentFunction.getVariable(id);
                if (variable == null)
                    throw new RuntimeException("returning unidentified identifier");
                sstack.push(variable);
                break;
            }
            case "returnFin": {
                ArrayList<Variable> return_tuple = new ArrayList<>();
                while (!sstack.peek().equals("return_start"))//reading return vars
                {
                    return_tuple.add(0, (Variable) sstack.pop());
                }
                sstack.pop();//pop return_start
                ArrayList<Variable> return_vars = currentFunction.getReturnVars();
                if (return_tuple.size() != currentFunction.return_types.size())
                    throw new RuntimeException("Return tuple size is not equal to the signature, expected: " + currentFunction.return_types.size() + " but found " + return_tuple.size());
                for (int i = 0; i < return_tuple.size(); i++) {
                    if (!(return_tuple.get(i).type.equals(currentFunction.return_types.get(i))))
                        throw new RuntimeException("return types are not equal to the signature");
                    makeIns(":=", return_tuple.get(i), return_vars.get(i));
                }
                sstack.push("dummy");//dummy for STMTFin
                end_current_function();
                break;
            }

            case "initTuple": {
                tupleSize = 1;
                break;
            }
            case "addTuple": {
                tupleSize++;
                break;
            }
            case "finTuple": {
                ArrayList<Object> tuple = new ArrayList<>();
                for (int i = 0; i < tupleSize; i++)
                    tuple.add(0, sstack.pop());
                sstack.push(tuple);
                break;
            }

            case "assignStructStart": {
                insideStructAssign = true;
                sstack.push("struct_assign");
                break;
            }
            case "assignStructEnd": {
                finishStructAssign();
                break;
            }
            case "assignStructEndEmpty": {
                sstack.push("");
                finishStructAssign();
                break;
            }
            case "assignStructEmpty": {
                sstack.push("");
                break;
            }
            case "structEnd": {
                insideStructDef = false;
                currentStruct = null;
                break;
            }

            case "subStruct": {
                Variable structVar = (Variable) sstack.pop();
                if (!(structVar.type instanceof StructType))
                    throw new RuntimeException("Variable is not of struct type");
                StructType structType = (StructType) structVar.type;
                Identifier id = (Identifier) currentToken;
                makeIns("+", makeConst(structType.getFieldOffset(id)), structVar, AX);
                Variable tempInt = currentFunction.getTemp(structType.getFieldType(id));
                assign(new Variable(Variable.ADDR_MODE.GLOBAL_INDIRECT, structType.getFieldType(id), AX.value), tempInt);
                sstack.push(tempInt);
                break;
            }

            case "structVar": {
                break;
            }
            case "structId": {
                insideStructDef = true;
                String struct_name = ((Identifier) currentToken).id;
                currentStruct = new StructType("type", struct_name);
                structsHashMap.put(struct_name, currentStruct);
                break;
            }


            case "envEnd": {
                insideEnvDef = false;
                currentFunction.currentEnv = Scope.DEFAULT_ENV;
                break;
            }

            case "envVar": {
                sstack.pop();//pop the declared variable
                break;
            }
            case "releaseId": {
                Variable relVar = getVariableFromId(currentToken);
                if (relVar.type instanceof StructType) { // and array
                    StructType structType = (StructType) relVar.type;
                    makeIns("gmm", relVar, makeConst(structType.getStructSize()));
                    assign(makeConst(0), relVar);
                } else if (relVar.type.isArray) {
                    makeIns("gmm", relVar, getArraySizeVar(relVar));
                    assign(makeConst(0), relVar);
                } else {
                    throw new RuntimeException("Can' release this kind of variable");
                }
                sstack.push("dummy");//dummy for STMTFin
                break;
            }
            case "assignArrayStart": {
                sstack.push("array_start");
                break;
            }
            case "assignArrayEnd": {

                ArrayList<Variable> dims = new ArrayList<>();
                while (!sstack.peek().equals("array_start"))//reading dimensions
                    dims.add(0, (Variable) sstack.pop());
                sstack.pop();//pop array_start
                Variable arrayVar = (Variable) sstack.pop();
                if (dims.size() != 1)
                    throw new RuntimeException("Incorrect dimensions");
                Variable size = dims.get(0);
                makeIns("*", size, makeConst(arrayVar.type.getTypeSize()), AX);
                assign(AX, getArraySizeVar(arrayVar));
                makeIns("gmm", AX, arrayVar);
                sstack.push("array_assign_fin");
                break;
            }
            case "startIndex": {
                sstack.push("index_start");
                break;
            }
            case "addIndex": {
                break;
            }
            case "finishIndex": {
                ArrayList<Variable> dims = new ArrayList<>();
                while (!sstack.peek().equals("index_start"))//reading dimensions
                    dims.add(0, (Variable) sstack.pop());
                sstack.pop();//pop index_start
                if (dims.size() != 1)
                    throw new RuntimeException("Incorrect dimensions");
                Variable arrayVar = (Variable) sstack.pop();
                Variable offset = dims.get(0);
                if (!arrayVar.type.isArray)
                    throw new RuntimeException("Variable type is not array");
                makeIns("*", makeConst(arrayVar.type.getTypeSize()), offset, AX);
                makeIns("+", AX, arrayVar, AX);
                sstack.push(new Variable(Variable.ADDR_MODE.GLOBAL_INDIRECT, arrayVar.type, AX.value));
                break;
            }

            case "envId": {
                insideEnvDef = true;
                currentFunction.currentEnv = ((Identifier) currentToken);
                currentFunction.getLastScope().envs.put(currentFunction.currentEnv, new HashMap<>());
                break;
            }

            case "labelId": {
                currentFunction.addLabel((Identifier) currentToken, PC);
                break;
            }
            case "gotoId": {
                currentFunction.getLastScope().labelJumps.put(PC, (Identifier) currentToken);
                instructions.add("");
                PC++;
                break;
            }
            case "blockStart": {
                currentFunction.scopes.add(new Scope());
                break;
            }

            case "blockFin": {
                for (Map.Entry<Integer, Identifier> entry : currentFunction.getLastScope().labelJumps.entrySet()) {
                    int pc = currentFunction.getLabel(entry.getValue());
                    if (pc == -1)
                        throw new RuntimeException("Label " + currentToken.parser_token + " doesn't exist for goto");
                    instructions.set(entry.getKey(), getIns("jmp", makeConst(pc)));
                }
                currentFunction.popLastScope();
                break;
            }

            case "caseExpr": {

                Variable expr = (Variable) sstack.pop();
                if (!expr.type.type.equals("int"))
                    throw new RuntimeException("Case expression should be integer");
                PC++;
                instructions.add("");//jmp to end of cases
                Case item = new Case(PC, expr);
                PC++;
                instructions.add("");//jmp to codes after the block
                currentFunction.caseStack.push(item);
                break;
            }
            case "caseBlock": {
                Case currentCase = currentFunction.caseStack.peek();
                makeIns("jmp", makeConst(currentCase.break_jmp_addr));
                break;
            }
            case "caseEnd": {
                Case currentCase = currentFunction.caseStack.peek();
                instructions.set(currentCase.break_jmp_addr - 1, getIns("jmp", makeConst(PC)));
                makeIns("<=", currentCase.expr, makeConst(currentCase.max_val), TMP_BOOL);
                makeIns("jz", TMP_BOOL, makeConst(currentCase.break_jmp_addr));
                makeIns("<=", makeConst(currentCase.min_val), currentCase.expr, TMP_BOOL);
                makeIns("jz", TMP_BOOL, makeConst(currentCase.break_jmp_addr));
                makeIns("-", currentCase.expr, makeConst(currentCase.min_val - (PC + 2)), AX);
                makeIns("jmp", AX);
                int tableStart = PC;
                for (int i = currentCase.min_val; i <= currentCase.max_val; i++)
                    makeIns("jmp", makeConst(currentCase.break_jmp_addr));
                // make jump table
                for (Map.Entry<Integer, Integer> entry : currentCase.const_to_addr.entrySet()) {
                    Integer calculated_postition = entry.getKey() - currentCase.min_val + tableStart;
                    instructions.set(calculated_postition, getIns("jmp", makeConst(entry.getValue())));
                }
                // fill final jump position
                instructions.set(currentCase.break_jmp_addr, getIns("jmp", makeConst(PC)));
                currentFunction.caseStack.pop();
                break;
            }
            case "caseConst": {
                Variable const_val = (Variable) sstack.pop();
                if (!const_val.type.type.equals("int"))
                    throw new RuntimeException("Cases should be integer");
                if (const_val.mode != Variable.ADDR_MODE.IMMEDIATE)
                    throw new RuntimeException("Cases should be constant values");
                Case currentCase = currentFunction.caseStack.peek();
                Integer key = Integer.valueOf(const_val.value);
                if (currentCase.const_to_addr.size() == 0) {
                    currentCase.min_val = key;
                    currentCase.max_val = key;
                }
                currentCase.min_val = Math.min(currentCase.min_val, key);
                currentCase.max_val = Math.max(currentCase.max_val, key);
                currentCase.const_to_addr.put(key, PC);
                break;
            }


            case "UNARY_NOT":
            case "COMPLEMENT":
            case "NEGATION":
            {
                Variable op1 = (Variable) sstack.pop();
                Variable tempVar = currentFunction.getTemp(op1.type);
                makeIns(opToString(sem), op1, tempVar);
                sstack.push(tempVar);
                break;
            }
            case "pushArg":
                return;
            default: {
                //System.out.println("Unknown sem = " + sem);
                throw new RuntimeException("Unknown sem = " + sem);
//                return;
            }

        }
    }

    private Variable getArraySizeVar(Variable arrayVar) {
        return new Variable(arrayVar.mode, intType, Integer.valueOf(arrayVar.value) + 4);
    }

    private void finishStructAssign() {
        ArrayList<Object> vars = new ArrayList<>();
        while (!sstack.peek().equals("struct_assign"))//reading return types
            vars.add(0, sstack.pop());
        sstack.pop();//popping struct_assign
        Variable structVar = (Variable) sstack.pop();
        if (!(structVar.type instanceof StructType)) {
            throw new RuntimeException("Assigning primitive type with struct assignment syntax");
        }
        StructType structType = (StructType) structVar.type;
        if (vars.size() != ((StructType) structVar.type).fields.size()) {
            throw new RuntimeException("Number of struct fields doesn't match with assignment expression");
        }
        makeIns("gmm", makeConst(structType.getStructSize()), structVar);
        for (int i = 0; i < structType.fields.size(); i++) {
            Map.Entry<Identifier, Type> field = structType.fields.get(i);
            makeIns("+", makeConst(structType.getFieldOffset(field.getKey())), structVar, AX);
            Object opr2 = vars.get(i);
            if (opr2.equals("")) {
                if (field.getValue() instanceof StructType)
                    assign(makeConst(0), new Variable(Variable.ADDR_MODE.GLOBAL_INDIRECT, field.getValue(), AX.value));
            } else
                assign((Variable) opr2, new Variable(Variable.ADDR_MODE.GLOBAL_INDIRECT, field.getValue(), AX.value));
        }
        sstack.push("struct_assign_fin");
        insideStructAssign = false;
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
                Variable tmpChar = currentFunction.getTemp(new PrimitiveType("type", "char"));
                assign(new Variable(Variable.ADDR_MODE.IMMEDIATE, new PrimitiveType("type", "int"), currentToken.value.charAt(0)), tmpChar);
                return tmpChar;
            }
            case "REAL": {
                return new Variable(Variable.ADDR_MODE.IMMEDIATE, new PrimitiveType("type", "real"), String.valueOf(Double.parseDouble(currentToken.value)));
            }
            case "HEX": {
                //Trim the leading 0x
                return new Variable(Variable.ADDR_MODE.IMMEDIATE, intType, Integer.parseInt(currentToken.value.substring(2), 16));
            }
            case "INT": {
                return new Variable(Variable.ADDR_MODE.IMMEDIATE, intType, Integer.parseInt(currentToken.value));
            }
            case "BOOL": {
                Variable tmpBool = currentFunction.getTemp(boolType);
                if (currentToken.value.equals("false")) {
                    assign(new Variable(Variable.ADDR_MODE.IMMEDIATE, boolType, "false"), tmpBool);
                    return tmpBool;
                } else if (currentToken.value.equals("true")) {
                    assign(new Variable(Variable.ADDR_MODE.IMMEDIATE, boolType, "true"), tmpBool);
                    return tmpBool;
                }
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
            if (!(arguments.get(i).type.equals(f.argsOrder.get(i).type)))
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
        if (currentFunction != null) {//not calling main itself
            ArrayList<Variable> return_vars = f.getReturnVars();
            ArrayList<Variable> result_vars = new ArrayList<>();
            for (Variable return_var : return_vars) {
                Variable result = currentFunction.getTemp(return_var.type);
                makeIns(":=", return_var, result);
                result_vars.add(result);
            }
            if (result_vars.size() == 1)
                sstack.push(result_vars.get(0));
            else
                sstack.push(result_vars);
        }
    }

    private String opToString(String op) {
        switch (op) {
            case "COMPLEMENT":
                return "~";
            case "UNARY_NOT":
                return "!";
            case "NEGATION":
                return "u-";
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

    public StructType getStruct(String yytext) {
        return (structsHashMap.getOrDefault(yytext, null));
    }

    void assign(Variable opr1, Object _opr2) {
        Variable opr2;
        opr2 = getVariableFromId(_opr2);
        if (opr1.type.isArray != opr2.type.isArray) {
            throw new RuntimeException("One type is array and another is not, can't assign them to each other");
        }
        makeIns(":=", opr1, opr2);
        if (opr1.type.isArray)
            makeIns(":=", getArraySizeVar(opr1), getArraySizeVar(opr2));
    }

    private Variable getVariableFromId(Object _opr2) {
        Variable opr2;
        if (_opr2 instanceof Identifier) {
            Identifier id = (Identifier) _opr2;
            opr2 = currentFunction.getVariable(id);
            if (opr2 == null)
                throw new RuntimeException("Assignment before declaration of variable " + id.id);
        } else
            opr2 = (Variable) _opr2;
        return opr2;
    }
}
