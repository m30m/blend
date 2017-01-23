/* JFlex example: partial Java language lexer specification */


%%

%class Lexer
%type String
%unicode
%line
%column

%{
  StringBuffer string = new StringBuffer();


%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment   = "<--"  ~"-->"

// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "--" {InputCharacter}* {LineTerminator}?

DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*

Identifier = [:jletter:] [:jletterdigit:]*

DecIntegerLiteral = 0 | [1-9][0-9]*

%state STRING

%%

/* keywords */
<YYINITIAL> "abstract"           { return string.toString(); }
<YYINITIAL> "boolean"            { return string.toString(); }
<YYINITIAL> "break"              { return string.toString(); }

<YYINITIAL> {
  /* identifiers */
  {Identifier}                   { return "ID"; } // { return "symbol(sym.IDENTIFIER);" }

  /* literals */
  {DecIntegerLiteral}            { return "integer"; } //{ return "symbol(sym.INTEGER_LITERAL);" }
  \"                             { string.setLength(0); yybegin(STRING); }

  /* operators */
  "="                            { return "ASSIGNMENT"; }
  "=="                           { return "EQUAL"; }
  "/"                            { return "BIN_DIV"; }
  "%"                            { return "BIN_MOD"; }
  "*"                            { return "BIN_MUL"; }
  "-"                            { return "BIN_SUB"; }
  "&"                            { return "BIT_AND"; }
  "^"                            { return "BIT_XOR"; }
  "|"                            { return "BIT_OR"; }
  "||"                           { return "BIN_OR"; }
  "&&"                           { return "BIN_AND"; }
  "+"                            { return "BIN_ADD"; }


  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

<STRING> {
  \"                             { yybegin(YYINITIAL);
                                   return
                                   string.toString(); }
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
