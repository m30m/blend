/* JFlex example: partial Java language lexer specification */


%%

%public
%class Scanner
%function NextToken
%type String
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
    return "$";
%eofval}



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
<YYINITIAL> "abstract"           { return yytext(); }
<YYINITIAL> "boolean"            { return yytext(); }
<YYINITIAL> "break"              { return yytext(); }

<YYINITIAL> {
  /* identifiers */
  {Identifier}                   { return "id"; } // { return "symbol(sym.IDENTIFIER);" }

  /* literals */
  {DecIntegerLiteral}            { return "integer"; } //{ return "symbol(sym.INTEGER_LITERAL);" }
  \"                             { string.setLength(0); yybegin(STRING); }

  /* operators */
  "="                            { return yytext(); }
  "=="                           { return yytext(); }
  "/"                            { return yytext(); }
  "%"                            { return yytext(); }
  "*"                            { return yytext(); }
  "-"                            { return yytext(); }
  "&"                            { return yytext(); }
  "^"                            { return yytext(); }
  "|"                            { return yytext(); }
  "||"                           { return yytext(); }
  "&&"                           { return yytext(); }
  "+"                            { return yytext(); }


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
