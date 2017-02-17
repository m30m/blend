/**
 * Created by amin on 2/16/17.
 */
class Variable {
    enum ADDR_MODE {GLOBAL_DIRECT, GLOBAL_INDIRECT, LOCAL_DIRECT, LOCAL_INDIRECT, IMMEDIATE}



    ADDR_MODE mode;
    Type type;
    String value;

    public Variable(ADDR_MODE mode, Type type, int value) {
        this.mode = mode;
        this.type = type;
        this.value = String.valueOf(value);
    }

    public Variable(ADDR_MODE mode, Type type, String value) {
        this.mode = mode;
        this.type = type;
        this.value = value;
    }

    public int getByteSize() {
        return type.getByteSize();
    }

    @Override
    public String toString() {
        String modeStr = modeToStr(mode);
        String typeStr = type.typeToVMStr();
        return modeStr + typeStr + value;
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
