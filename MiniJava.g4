grammar MiniJava;


@parser::header {
import java.util.ArrayList;
import java.util.Optional;
import edu.westminstercollege.cmpt355.minijava.node.*;
}


goal
returns [Block n]
   : methodBody EOF {
       $n = $methodBody.n;
   }
   ;


methodBody
returns [Block n]
   : (stmts+=statement)* {
       var statements = new ArrayList<Statement>();
       for (var stmt : $stmts)
           statements.add(stmt.n);
       $n = new Block($ctx, statements);
   }
   ;


statement
returns [Statement n]
   : ';' {
       $n = new EmptyStatement($ctx);
   }
   | '{' (stmts+=statement)* '}' {
       var statements = new ArrayList<Statement>();
       for (var stmt : $stmts)
           statements.add(stmt.n);
       $n = new Block($ctx, statements);
   }
   | declaration {
       $n = $declaration.n;
   }
   | expression ';' {
       $n = new ExpressionStatement($ctx, $expression.n);
   }
   ;


declaration
returns [VariableDeclarations n]
   : type decls+=declarationItem (',' decls+=declarationItem)* ';' {
       var items = new ArrayList<DeclarationItem>();
       for (var decl : $decls)
           items.add(decl.n);
       $n = new VariableDeclarations($ctx, $type.n, items);
   }
   ;


declarationItem
returns [DeclarationItem n]
   : NAME {
       $n = new DeclarationItem($ctx, $NAME.text, Optional.empty());
   }
   | NAME '=' expression {
       $n = new DeclarationItem($ctx, $NAME.text, Optional.of($expression.n));
   }
   ;


type
returns [TypeNode n]
   : primitiveType {
       $n = $primitiveType.n;
   }
   | NAME {
       $n = new TypeNode($ctx, $NAME.text);
   }
   ;


primitiveType
returns [TypeNode n]
   : t=('int' | 'double' | 'boolean') {
       $n = new TypeNode($ctx, $t.text);
   }
   ;


expression
returns [Expression n]
   : print {
       $n = $print.n;
   }
   | INT {
       $n = new IntLiteral($ctx, $INT.text);
   }
   | DOUBLE {
       $n = new DoubleLiteral($ctx, $DOUBLE.text);
   }
   | BOOLEAN {
       $n = new BooleanLiteral($ctx, $BOOLEAN.text);
   }
   | STRING {
       $n = new StringLiteral($ctx, $STRING.text);
   }
   | NAME {
       $n = new VariableAccess($ctx, $NAME.text);
   }
   | '(' expression ')' {
       $n = $expression.n;
   }
   | l=expression op=('++' | '--') {
       $n = new PostIncrement($ctx, $l.n, $op.text);
   }
   | op=('++' | '--' | '+' | '-') expression {
       if ($op.text.equals("++") || $op.text.equals("--"))
           $n = new PreIncrement($ctx, $expression.n, $op.text);
       else if ($op.text.equals("-"))
           $n = new Negate($ctx, $expression.n);
       else
           $n = $expression.n;
   }
   | '(' type ')' expression {
       $n = new Cast($ctx, $type.n, $expression.n);
   }
   | l=expression op=('*' | '/' | '%') r=expression {
       $n = new BinaryOp($ctx, $l.n, $r.n, $op.text);
   }
   | l=expression op=('+' | '-') r=expression {
       $n = new BinaryOp($ctx, $l.n, $r.n, $op.text);
   }
   | <assoc=right> lhs=expression '=' rhs=expression {
       $n = new Assignment($ctx, $lhs.n, $rhs.n);
   }
   ;


print
returns [Print n]
   : 'print' '(' (args+=expression (',' args+=expression)*)? ')' {
       var arguments = new ArrayList<Expression>();
       for (var arg : $args)
           arguments.add(arg.n);
       $n = new Print($ctx, arguments);
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


fragment DIGITS
   : [0-9]+;


fragment REAL
   : [0-9]+ ('.' [0-9]*)
   | [0-9]* '.' [0-9]+
   ;


fragment EXPONENT
   : [Ee] [+-]? DIGITS
   ;


INT
   : DIGITS
   ;


DOUBLE
   : REAL EXPONENT?
   | DIGITS EXPONENT?
   ;


BOOLEAN
   : 'true'
   | 'false'
   ;


STRING
   : '"' .*? '"'
   ;


NAME
   : [a-zA-Z_$] [a-zA-Z0-9_$]*
   ;


LINE_COMMENT
   : '//' .*? ('\n' | EOF) -> skip
   ;


BLOCK_COMMENT
   : '/*' .*? '*/' -> skip
   ;


WHITESPACE
   : [ \r\n\t]+    -> skip
   ;
