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
}

interface Type
{
    int getByteSize();
}

class PrimitiveType extends Token implements Type{
    String type;

    public PrimitiveType(String parser_token, String type) {
        super(parser_token);
        this.type = type;
    }

    public int getByteSize() {
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
}