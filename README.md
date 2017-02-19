# Blend Compiler

This project was developed, using an incremental approach.
For each part of the language, first the grammer was implemented using PGen 3 and afterwards code generation was done.
`git log` may show how this was done.

### Parts that remained to be done:
1. Casting different types
1. String type
1. Castring string to character array
1. `strlen` and `concat`
1. `long` type
1. Combined arithmetics
1. Global variables
1. `late` and `out` specifiers

### Important design decisions
Here are some of the important parts of code generation and resolving ambiguity in the grammer:

#### Differentiating `type` and `id`:
Since some `id`s could be changed to `type` later by the `struct` syntax,
I changed the scanner to ask from the code generator whether each `id` was recorded as a `type` before. Depending on the answer,
return value of scanner would be changed:
```
if(Parser.cg.getStruct(yytext())!=null)
  {return Parser.cg.getStruct(yytext());}
return new Identifier("id", yytext());
```

The same trick was used again for handling the ambiguity in struct assignment with `>` character,
I used a different token(`assignEnd`) in PGen when expecting the struct assignment to be finished:
```
if(Parser.cg.isInsideStructAssign())
  {return new Token("assignEnd");}
return new Token(yytext());
```

#### Using several stacks
Several stacks like semantic stack, loop stack, case stack and scope stack were used for different parts of code generation.
These stack help to resolve recursive syntaxes like two loops within eachother.

#### Using java OOP
Java OOP helped us handling different types and variables, two key classes using within all parts of the program were
`Variable` and `Type` classes which hold information about the mode and type of variables. For example the variable is `int`,
the addressing mode is indirect and the position of it is X.

Scanner returned different objects all inherited from the `Token` class. `Literal`, `Identifier` and `Type` were all it's subclasses,
each with it's own properties.

#### The Semantic Stack
Semantic stack plays a keyrole in code gneration, I used a generalized stack which could contain any `Object` in order to
put different kinds of objects in it. A pattern which repeated several times in different parts of the codes was pushing a "flag"
string in the beginning of a complex syntax, pushing different kinds of objects during handling the syntax and popping all the objects
until reaching the flag again at the end of the syntax.


