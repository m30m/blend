import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by amin on 2/16/17.
 */

class Scope {
    public static final Identifier DEFAULT_ENV = new Identifier("id", "global");

    HashMap<Identifier, Integer> labels;
    HashMap<Identifier, HashMap<Identifier, Variable>> envs;
    HashMap<Integer, Identifier> labelJumps;

    public Scope() {
        labels = new HashMap<>();
        envs = new HashMap<>();
        labelJumps = new HashMap<>();
        envs.put(DEFAULT_ENV, new HashMap<>());
    }
}

class Function {
    static int RETURN_START_ADDR = 2000;
    int argVarsSize;
    ArrayList<Type> return_types;
    ArrayList<Variable> argsOrder;//FIXME
    ArrayList<Scope> scopes;
    int localVarsSize = 0;
    Identifier currentEnv = Scope.DEFAULT_ENV;
    Identifier id;
    int start_PC;
    Variable return_addr;
    Variable old_base_pointer;

    Stack<Loop> loopStack = new Stack<>();
    Stack<Case> caseStack = new Stack<>();

    public Function() {
        return_types = new ArrayList<>();
        scopes = new ArrayList<>();
        scopes.add(new Scope());
        argsOrder = new ArrayList<>();
        old_base_pointer = new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, new PrimitiveType("type", "int"), 0);
        return_addr = new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, new PrimitiveType("type", "int"), 4);
        argVarsSize = 8;//return address and old base pointer
        localVarsSize = 0;
    }

    public void addArgument(Type t, Identifier argId) {
        if (scopes.get(0).envs.get(Scope.DEFAULT_ENV).containsKey(argId))
            throw new RuntimeException("Arguments with the same name!");
        Variable arg = new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, t, argVarsSize);
        argVarsSize += t.getByteSize();
        scopes.get(0).envs.get(Scope.DEFAULT_ENV).put(argId, arg);
        argsOrder.add(arg);
    }

    public Variable addVariable(Type t, Identifier varId) {
        if (containsKey(varId))
            throw new RuntimeException("Duplicate declaration of " + varId.id + " in function " + id.id);
        localVarsSize += t.getByteSize();
        Variable v = new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, t, -localVarsSize);
        getLastScope().envs.get(currentEnv).put(varId, v);
        return v;
    }

    public void addLabel(Identifier varId, int PC) {
        if (containsLabel(varId))
            throw new RuntimeException("Duplicate declaration of label " + varId.id + " in function " + id.id);
        scopes.get(scopes.size() - 1).labels.put(varId, PC);
    }

    boolean containsKey(Identifier varId) {
        return containsKey(varId, Scope.DEFAULT_ENV);
    }

    boolean containsKey(Identifier varId, Identifier envId) {
        for (Scope scope : scopes) {
            if (scope.envs.containsKey(envId) && scope.envs.get(envId).containsKey(varId))
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
        return new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, new PrimitiveType("type", "int"), -localVarsSize);
    }

    Variable getTemp(Type type) {
        localVarsSize += type.getByteSize();
        return new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, type, -localVarsSize);
    }

    Variable getTempBool() {
        localVarsSize += 1;
        return new Variable(Variable.ADDR_MODE.LOCAL_DIRECT, new PrimitiveType("type", "bool"), -localVarsSize);
    }

    public Variable getVariable(Identifier varId) {
        return getVariable(varId, Scope.DEFAULT_ENV);
    }

    public Variable getVariable(Identifier varId, Identifier envId) {
        for (int i = scopes.size() - 1; i >= 0; i--)
            if (scopes.get(i).envs.containsKey(envId) && scopes.get(i).envs.get(envId).containsKey(varId))
                return scopes.get(i).envs.get(envId).get(varId);
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
        scopes.remove(scopes.size() - 1);
    }

    public ArrayList<Variable> getReturnVars() {
        int offset = RETURN_START_ADDR;
        ArrayList<Variable> return_vars = new ArrayList<>();
        for (Type return_type : return_types) {
            return_vars.add(new Variable(Variable.ADDR_MODE.GLOBAL_DIRECT, return_type, offset));
            offset += return_type.getByteSize();
        }
        return return_vars;
    }
}
