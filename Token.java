/**
 * Created by amin on 1/23/17.
 */


public class Token {
    String parser_token;

    public Token(String parser_token) {
        this.parser_token = parser_token;
    }
}

class Identifier extends Token {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identifier that = (Identifier) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    String id;

    public Identifier(String parser_token, String id) {
        super(parser_token);
        this.id = id;
    }
}

class Literal extends Token {
    String type;
    String value;

    public Literal(String parser_token, String type, String value) {
        super(parser_token);
        this.type = type;
        this.value = value;
    }
    public Literal(String parser_token, String type, char value) {
        super(parser_token);
        this.type = type;
        this.value = String.valueOf(value);
    }
}

abstract class Type extends Token {
    String type;
    boolean isArray;

    public Type(String parser_token, String type, boolean isArray) {
        super(parser_token);
        this.type = type;
        this.isArray = isArray;
    }

    public Type(String parser_token, String type) {
        super(parser_token);
        this.type = type;
        this.isArray = false;
    }

    abstract int getTypeSize();
    int getByteSize()
    {
        if(isArray)
            return 8;
        return getTypeSize();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Type type1 = (Type) o;

        if (isArray != type1.isArray) return false;
        return type != null ? type.equals(type1.type) : type1.type == null;

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (isArray ? 1 : 0);
        return result;
    }

    abstract String typeToVMStr();
}

class PrimitiveType extends Type {


    public PrimitiveType(String parser_token, String type) {
        super(parser_token, type);
    }

    public int getTypeSize() {
        if (isArray)
            return 4;
        switch (type) {
            case "string":
                return 4;
            case "int":
                return 4;
            case "real":
                return 4;
            case "char":
                return 1;
            case "bool":
                return 1;
            case "long":
                return 8;
        }
        throw new RuntimeException("Unknown primitive type");
    }

    String typeToVMStr() {
        switch (this.type) {
            case "int":
                return "i_";
            case "real":
                return "f_";
            case "bool":
                return "b_";
            case "string":
                return "s_";
            case "char":
                return "c_";
        }
        return "ERR";
    }
}