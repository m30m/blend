/**
 * Created by amin on 2/16/17.
 */
class Variable {
    enum ADDR_MODE {GLOBAL_DIRECT, GLOBAL_INDIRECT, LOCAL_DIRECT, LOCAL_INDIRECT, IMMEDIATE}

    enum TYPE {INT, REAL, BOOL, STRING, CHAR}


    ADDR_MODE mode;
    TYPE type;
    String value;

    public Variable(ADDR_MODE mode, TYPE type, int value) {
        this.mode = mode;
        this.type = type;
        this.value = String.valueOf(value);
    }

    public Variable(ADDR_MODE mode, TYPE type, String value) {
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
