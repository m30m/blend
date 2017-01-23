import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;

class Variable {
    enum ADDR_MODE {GLOBAL_DIRECT, GLOBAL_INDIRECT, LOCAL_DIRECT, LOCAL_INDIRECT, IMMEDIATE}

    enum TYPE {INTEGER, FLOAT, BOOLEAN, STRING, CHAR}


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

            case INTEGER:
                return "i_";
            case FLOAT:
                return "f_";
            case BOOLEAN:
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

public class CodeGenerator {
    Scanner scanner; // This was my way of informing CG about Constant Values detected by Scanner, you can do whatever you like

    Stack<Object> sstack;

    // Define any variables needed for code generation

    String code;
    private int tmp_addr;

    public CodeGenerator(Scanner scanner) {
        this.scanner = scanner;
        this.sstack = new Stack<>();
        this.code = "";
    }

    public void makeIns(String opCode, Variable opr3, Variable opr1, Variable opr2) {
        String ins = opCode + " " + opr1 + " " + opr2 + " " + opr3 + "\n";
        code = code + ins;
    }

    public void makeIns(String opCode, Variable opr2, Variable opr1) {
        String ins = opCode + " " + opr1 + " " + opr2 + "\n";
        code = code + ins;
    }

    public void makeIns(String opCode, Variable opr1) {
        String ins = opCode + " " + opr1 + "\n";
        code = code + ins;
    }

    public void Generate(String sem) {
        System.out.println(sem); // Just for debug

        switch (sem) {
            case "NoSem":
                return;
            case "push": {
                Variable v = getTempInt();
                Variable five = new Variable(Variable.ADDR_MODE.IMMEDIATE, Variable.TYPE.INTEGER, 5);
                makeIns(":=", v, five);
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

            case "UNARY_NOT":
            case "COMPLEMENT":
            case "NEGATION":

                return;

        }
    }

    private String opToString(String op) {
        switch (op)
        {
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
            return "NEQ":
                return "!"
        }
        return "ERR";
    }

    private Variable getTempInt() {
        return new Variable(Variable.ADDR_MODE.GLOBAL_DIRECT, Variable.TYPE.INTEGER, getTempAddr());
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
            writer.print(code);
            writer.close();
        } catch (IOException e) {
            // do something
        }
    }
}
