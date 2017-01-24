/**
 * Created by amin on 1/23/17.
 */


public class Token {
    String parser_token;

    public Token(String parser_token) {
        this.parser_token = parser_token;
    }
}

class Identifier extends Token
{
    String id;

    public Identifier(String parser_token, String id) {
        super(parser_token);
        this.id = id;
    }
}

class Literal extends Token
{
    String type;
    String value;

    public Literal(String parser_token, String type, String value) {
        super(parser_token);
        this.type = type;
        this.value = value;
    }
}

class Type extends Token
{
    String type;

    public Type(String parser_token, String type) {
        super(parser_token);
        this.type = type;
    }
}