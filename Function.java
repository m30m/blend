import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by amin on 2/16/17.
 */
class Function {
    int argVarsSize;
    ArrayList<Type> return_types;
    ArrayList<Variable> argsOrder;//FIXME
    HashMap<Identifier, Variable> vars;
    int localVarsSize = 0;
    Identifier id;
    int start_PC;
    Variable return_addr;
    Variable old_base_pointer;

    Stack<Loop> loopStack;

    public Function() {
        return_types = new ArrayList<>();
        vars = new HashMap<>();
        argsOrder = new ArrayList<>();
        loopStack = new Stack<>();
        old_base_pointer = new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, Variable.TYPE.INT, 0);
        return_addr = new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, Variable.TYPE.INT, 4);
        argVarsSize = 8;//return address and old base pointer
        localVarsSize = 0;
    }

    public void addArgument(Type t, Identifier argId) {
        if (vars.containsKey(argId))
            throw new RuntimeException("Arguments with the same name!");
        Variable arg = new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, Variable.TYPE.valueOf(t.type.toUpperCase()), argVarsSize);
        argVarsSize += t.getByteSize();
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

    Variable getTempInt() {
        localVarsSize += 4;
        return new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, Variable.TYPE.INT, -localVarsSize);
    }

    Variable getTemp(Variable.TYPE type, int size) {
        localVarsSize += size;
        return new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, type, -localVarsSize);
    }

    Variable getTempBool() {
        localVarsSize += 1;
        return new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, Variable.TYPE.BOOL, -localVarsSize);
    }

    public Variable getVariable(Identifier varId) {
        if (!vars.containsKey(varId))
            return null;
        return vars.get(varId);
    }
}
