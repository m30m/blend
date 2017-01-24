/* JFlex example: partial Java language lexer specification */


%%

%public
%class Scanner
%function NextToken
%type Token
%unicode
%line
%column

%{
    StringBuffer string = new StringBuffer();

    public int lineNumber() {
        return yyline;
    }
%}

%eofval{
    return new Token("$");
%eofval}



LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f\v]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment   = "<--"  ~"-->"

// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "--" {InputCharacter}* {LineTerminator}?

DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*

Identifier = [:jletter:] [:jletterdigit:]*

DecIntegerLiteral = [0-9]+
HexIntegerLiteral = 0x[0-9]*
RealLiteral = [0-9]+\.[0-9]* | [0-9]*\.[0-9]+
CharLiteral = '[^']' | '\''

%state STRING

%%

/* keywords */
<YYINITIAL> "array"                     { return new Token(yytext()); }
<YYINITIAL> "assign"                    { return new Token(yytext()); }
<YYINITIAL> "bool"                      { return new Type("type", yytext()); }
<YYINITIAL> "break"                     { return new Token(yytext()); }
<YYINITIAL> "case"                      { return new Token(yytext()); }
<YYINITIAL> "char"                      { return new Type("type", yytext()); }
<YYINITIAL> "continue"                  { return new Token(yytext()); }
<YYINITIAL> "do"                        { return new Token(yytext()); }
<YYINITIAL> "else"                      { return new Token(yytext()); }
<YYINITIAL> "endcase"                   { return new Token(yytext()); }
<YYINITIAL> "environment"               { return new Token(yytext()); }
<YYINITIAL> "false"                     { return new Token(yytext()); }
<YYINITIAL> "function"                  { return new Token(yytext()); }
<YYINITIAL> "goto"                      { return new Token(yytext()); }
<YYINITIAL> "if"                        { return new Token(yytext()); }
<YYINITIAL> "int"                       { return new Type("type", yytext()); }
<YYINITIAL> "isvoid"                    { return new Token(yytext()); }
<YYINITIAL> "label"                     { return new Token(yytext()); }
<YYINITIAL> "late"                      { return new Token(yytext()); }
<YYINITIAL> "long"                      { return new Type("type", yytext()); }
<YYINITIAL> "of"                        { return new Token(yytext()); }
<YYINITIAL> "out"                       { return new Token(yytext()); }
<YYINITIAL> "real"                      { return new Type("type", yytext()); }
<YYINITIAL> "release"                   { return new Token(yytext()); }
<YYINITIAL> "return"                    { return new Token(yytext()); }
<YYINITIAL> "string"                    { return new Type("type", yytext()); }
<YYINITIAL> "structure"                 { return new Token(yytext()); }
<YYINITIAL> "true"                      { return new Token(yytext()); }
<YYINITIAL> "void"                      { return new Token(yytext()); }
<YYINITIAL> "while"                     { return new Token(yytext()); }

<YYINITIAL> {
  /* identifiers */
  {Identifier}                   { return new Identifier("id", yytext()); }

  /* literals */
  {HexIntegerLiteral}            { return new Literal("const", "HEX", yytext()); }
  {RealLiteral}                  { return new Literal("const", "REAL", yytext()); }
  {CharLiteral}                  { return new Literal("const", "CHAR", yytext()); }
  {DecIntegerLiteral}            { return new Literal("const", "INT", yytext()); }
  \"                             { string.setLength(0); yybegin(STRING); }

  /* syntax */
  ","                            { return new Token(yytext()); }
  "["                            { return new Token(yytext()); }
  "]"                            { return new Token(yytext()); }
  ";"                            { return new Token(yytext()); }
  "{"                            { return new Token(yytext()); }
  "}"                            { return new Token(yytext()); }
  "::"                           { return new Token(yytext()); }
  "("                            { return new Token(yytext()); }
  ")"                            { return new Token(yytext()); }

  /* operators */
  ":="                           { return new Token(yytext()); }

  "/"                            { return new Token(yytext()); }
  "%"                            { return new Token(yytext()); }
  "*"                            { return new Token(yytext()); }
  "-"                            { return new Token(yytext()); }
  "&"                            { return new Token(yytext()); }
  "^"                            { return new Token(yytext()); }
  "|"                            { return new Token(yytext()); }
  "||"                           { return new Token(yytext()); }
  "&&"                           { return new Token(yytext()); }
  "+"                            { return new Token(yytext()); }
  "~"                            { return new Token(yytext()); }
  "!"                            { return new Token(yytext()); }

  "="                            { return new Token(yytext()); }
  "<="                           { return new Token(yytext()); }
  ">="                           { return new Token(yytext()); }
  "!="                           { return new Token(yytext()); }
  "<"                            { return new Token(yytext()); }
  ">"                            { return new Token(yytext()); }




  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

<STRING> {
  \"                             { yybegin(YYINITIAL);
                                   return
                                   new Literal("const", "STRING", string.toString()); }
  [^\n\r\"\\]+                   { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }

  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }
  \\                             { string.append('\\'); }
}

/* error fallback */
[^]                              { throw new Error("Illegal character <"+
                                                    yytext()+">"); }
