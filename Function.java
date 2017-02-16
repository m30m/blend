import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by amin on 2/16/17.
 */

class Scope {
    HashMap<Identifier, Variable> vars;
    HashMap<Identifier, Integer> labels;
    HashMap<Integer, Identifier> labelJumps;

    public Scope() {
        vars = new HashMap<>();
        labels = new HashMap<>();
        labelJumps = new HashMap<>();
    }
}

class Function {
    int argVarsSize;
    ArrayList<PrimitiveType> return_types;
    ArrayList<Variable> argsOrder;//FIXME
    ArrayList<Scope> scopes;
    int localVarsSize = 0;
    Identifier id;
    int start_PC;
    Variable return_addr;
    Variable old_base_pointer;

    Stack<Loop> loopStack;

    public Function() {
        return_types = new ArrayList<>();
        scopes = new ArrayList<>();
        scopes.add(new Scope());
        argsOrder = new ArrayList<>();
        loopStack = new Stack<>();
        old_base_pointer = new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, Variable.TYPE.INT, 0);
        return_addr = new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, Variable.TYPE.INT, 4);
        argVarsSize = 8;//return address and old base pointer
        localVarsSize = 0;
    }

    public void addArgument(PrimitiveType t, Identifier argId) {
        if (scopes.get(0).vars.containsKey(argId))
            throw new RuntimeException("Arguments with the same name!");
        Variable arg = new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, Variable.TYPE.valueOf(t.type.toUpperCase()), argVarsSize);
        argVarsSize += t.getByteSize();
        scopes.get(0).vars.put(argId, arg);
        argsOrder.add(arg);
    }

    public Variable addVariable(PrimitiveType t, Identifier varId) {
        if (containsKey(varId))
            throw new RuntimeException("Duplicate declaration of " + varId.id + " in function " + id.id);
        localVarsSize += t.getByteSize();
        Variable v = new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, Variable.TYPE.valueOf(t.type.toUpperCase()), -localVarsSize);
        getLastScope().vars.put(varId, v);
        return v;
    }

    public void addLabel(Identifier varId, int PC) {
        if (containsLabel(varId))
            throw new RuntimeException("Duplicate declaration of label " + varId.id + " in function " + id.id);
        scopes.get(scopes.size() - 1).labels.put(varId, PC);
    }

    boolean containsKey(Identifier varId) {
        for (Scope scope : scopes) {
            if (scope.vars.containsKey(varId))
                return true;
        }
        return false;
    }

    boolean containsLabel(Identifier varId) {
        for (Scope scope : scopes) {
            if (scope.labels.containsKey(varId))
                return true;
        }
        return false;
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
        for (int i = scopes.size() - 1; i >= 0; i--)
            if (scopes.get(i).vars.containsKey(varId))
                return scopes.get(i).vars.get(varId);
        return null;
    }

    Scope getLastScope() {
        return scopes.get(scopes.size() - 1);
    }

    public int getLabel(Identifier varId) {
        if (getLastScope().labels.containsKey(varId))
            return getLastScope().labels.get(varId);
        return -1;
    }

    public void popLastScope() {
        scopes.remove(scopes.size()-1);
    }
}
