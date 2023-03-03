grammar MiniJava;

    @parser::header {
import edu.westminstercollege.cmpt355.minijava.node.*;    }

goal
returns [Block n]
   : mbod=methodBody EOF {
        $n = $mbod.n;
   }
   ;


methodBody
returns [Block n]
   : (mbod += statement)* {
       var statements = new ArrayList<Statement>();
       for (var mbods : $mbod)
                   statements.add(mbods.n);
   $n = new Block(statements);
   }
   ;


statement
returns [Statement n]
   : ';' {
        $n = new EmptyStatement();
   }
   | '{' statement* '}' {
        var statements = new ArrayList<Statement>();
        for (var mbods : $mbod)
            statements.add(mbods.n);
        $n = new Block(statements);
   }
   | variableDeclaration {
       $n = new VarDeclaration(TypeNode, VarDeclarationInit);
   }
   | expression ';'{
       $n = $expression.n;
   }
   ;


// type followed by a comma-separated list of "items", each being just a name or a name = value
variableDeclaration
returns [VariableDeclaration n]
   : type dec+=variableDeclarationItem (',' dec+=variableDeclarationItem)* ';' {
       var declarations = new ArrayList<variableDeclarationItem>();
               for (var dec : $dec)
                   declarations.add(dec.n);
               $n = new Input(declarations);
   }
   ;


variableDeclarationItem
returns [VariableDeclarationItem n]
   : NAME {
       $n = new Assignment($NAME.text);
   }
   | NAME '=' expression {
       $n = new Assignment($NAME.text, $expression.n);
   }
   ;


expression
returns [Expression n]
   : 'print' '(' (expression (',' expression)*)? ')'
   | INT {
       $n = new IntLiteral($INT.text);
   }
   | DOUBLE {
       $n = new DoubleLiteral($DOUBLE.text);
   }
   | STRING {
       $n = new StringLiteral($STRING.text);
   }
   | NAME {
       $n = new VariableAccess($NAME.text);
   }
   | '(' expression ')' {
       $n = $expression.n;
   }
   | expression ('++' | '--') {
       $n = $expression.n;
   }
   | ('++' | '--' | '+' | '-') expression {
       $n = $expression.n;
   }
   | '(' type ')' expression
   | expression ('*' | '/' | '*') expression{
       $n = $expression.n;
   }
   | expression ('+' | '-') expression {
       $n = $expression.n;
   }
   | <assoc=right> expression '=' expression {
       $n = $expression.n;
   }
   ;


type
returns [Type n]
   : 'int' {
       $n = "int";
   }
   | 'double' {
       $n = "double";
   }
   | 'boolean' {
       $n = "boolean";
   }
   | 'String' {
       $n = "String";
   }
   | NAME {
       $n = new VariableAcces($NAME.text);
   }
   ;



RESERVED_WORD
   : 'abstract'   | 'continue'   | 'for'          | 'new'         | 'switch'
   | 'assert'     | 'default'    | 'if'           | 'package'     | 'synchronized'
   | 'boolean'    | 'do'         | 'goto'         | 'private'     | 'this'
   | 'break'      | 'double'     | 'implements'   | 'protected'   | 'throw'
   | 'byte'       | 'else'       | 'import'       | 'public'      | 'throws'
   | 'case'       | 'enum'       | 'instanceof'   | 'return'      | 'transient'
   | 'catch'      | 'extends'    | 'int'          | 'short'       | 'try'
   | 'char'       | 'final'      | 'interface'    | 'static'      | 'void'
   | 'class'      | 'finally'    | 'long'         | 'strictfp'    | 'volatile'
   | 'const'      | 'float'      | 'native'       | 'super'       | 'while'
   | '_'
   ;


// letters, numbers, dollar signs '$', underscores'_', but not starting with a digit
NAME
   : [a-zA-z_$] [a-zA-z_$0-9]*
   ;


WHITESPACE
   : [ \n\r\t] -> skip
   ;


fragment DIGITS
   : [0-9]+
   ;


fragment FIXED_POINT
   : [0-9]+ '.' [0-9]*
   | [0-9]* '.' [0-9]+
   ;


INT
   : DIGITS
   | [0-9]+
   ;


DOUBLE
   : FIXED_POINT
   | FIXED_POINT [Ee] [+-]? INT
   | DIGITS [Ee] [+-]? DIGITS
   ;


BOOLEAN
   : 'true'
   | 'false'
   ;


STRING
   : '"' .*? '"'
   ;


LINE_COMMENT
   : '//' .*? ('\n' | EOF) -> skip
   ;


BLOCK_COMMENT
   : '/*' .*? '*/'         -> skip
   ;

