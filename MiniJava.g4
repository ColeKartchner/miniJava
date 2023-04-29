grammar MiniJava;


@parser::header {
import java.util.ArrayList;
import java.util.Optional;
import edu.westminstercollege.cmpt355.minijava.node.*;
import edu.westminstercollege.cmpt355.minijava.*;
}


goal
returns [ClassNode n]
   : classNode EOF {
       $n = $classNode.n;
   }
   ;


classNode
returns [ClassNode n]
   : (imports+=innport)* (fields+=field)* (methods+=method)* (mainMethod+=main)? EOF {
       var importList = new ArrayList<Import>();
       var fieldList = new ArrayList<FieldDefinition>();
       var methodList = new ArrayList<MethodDefinition>();
       Optional<MainMethodDefinition> mainOptional = Optional.empty();




       for (var imp : $imports)
           importList.add(imp.n);
       for (var field : $fields)
           fieldList.add(field.n);
       for (var method : $methods)
           methodList.add(method.n);
       for (var mainM : $mainMethod)
           mainOptional.of(mainM.n);


       if ($main.ctx != null)


           $n = new ClassNode($ctx, importList, fieldList, methodList, mainOptional);
       else
           $n = new ClassNode($ctx, importList, fieldList, methodList, Optional.empty());


   }
   ;




method
returns [MethodDefinition n]
   : type NAME '(' (params+=parameter (',' params+=parameter )*)? ')' methodBody {
       var parameters = new ArrayList<Parameter>();
       for (var param : $params)
           parameters.add(param.n);
       $n = new MethodDefinition($ctx, $type.n, $NAME.text, parameters, $methodBody.n, new SymbolTable(SymbolTable.Level.Method));
   }
   | 'void' NAME '(' (params+=parameter (',' params+=parameter )*)? ')' methodBody {
       var parameters = new ArrayList<Parameter>();
               for (var param : $params)
                   parameters.add(param.n);
               $n = new MethodDefinition($ctx, new TypeNode($ctx, VoidType.Instance), $NAME.text, parameters, $methodBody.n, new SymbolTable(SymbolTable.Level.Method));
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


main
   returns [MainMethodDefinition n]
   : 'main' '(' ')' '{' methodBody '}' {
       $n = new MainMethodDefinition($ctx, $methodBody.n, new SymbolTable(SymbolTable.Level.Method));
   }
   ;


parameter
returns [Parameter n]
   : type NAME {
       $n = new Parameter($ctx, $type.n, $NAME.text);
   }
   ;


field
   returns [FieldDefinition n]
   : type NAME ';' {
       $n = new FieldDefinition($ctx, $type.n, $NAME.text, Optional.empty());
   }
   | type NAME '=' e=expression ';' {
       $n = new FieldDefinition($ctx, $type.n, $NAME.text, Optional.of($e.n));
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
   | 'return' ';' {
       $n = new Return($ctx, Optional.empty());
   }
   | 'return' e=expression ';' {
       $n = new Return($ctx, Optional.of($e.n));
   }
   ;


innport
returns [Import n]
   : 'import' imNames+=NAME ('.' imNames+=NAME)* '.' ';' {
       String importPath = "";
       for (var imName : $imNames) {
           importPath += imName.getText();
       }
       $n = new PackageImport($ctx, importPath);
   }
   | 'import' imNames+=NAME ('.' imNames+=NAME)* '.' '*' ';' {
            String importPath = "";
            for (var imName : $imNames) {
                importPath += imName.getText();
            }
            $n = new PackageImport($ctx, importPath);
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
       $n = new TypeNode($ctx, new ClassType($NAME.text));
   }
   ;


primitiveType
returns [TypeNode n]
   : 'int' {
       $n = new TypeNode($ctx, PrimitiveType.Int);
   }
   | 'double' {
       $n = new TypeNode($ctx, PrimitiveType.Double);
   }
   | 'boolean' {
       $n = new TypeNode($ctx, PrimitiveType.Boolean);
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
       // Blah blah
       $n = new VariableAccess($ctx, $NAME.text);
   }
   | e=expression '.' NAME {
       $n = new FieldAccess($ctx, $e.n, $NAME.text);
   }
   | callee=expression '.' NAME '(' (args+=expression (',' args+=expression)*)? ')' {
       var arguments = new ArrayList<Expression>();
       for (var arg : $args)
         arguments.add(arg.n);
       $n = new MethodCall($ctx, $callee.n, $NAME.text, arguments);
   }
   | 'new' NAME '(' (args+=expression (',' args+=expression)*)? ')' {
       var arguments = new ArrayList<Expression>();
       for (var arg : $args)
           arguments.add(arg.n);
       $n = new ConstructorCall($ctx, $NAME.text, arguments);
   }
   | '(' expression ')' {
       $n = $expression.n;
   }
   | l=expression op=('++' | '--') {
       $n = new PostIncrement($ctx, (LValue)$l.n, $op.text);
       //$n = new PostIncrement(new VariableAccess("dummy"), $op.text);
   }
   | op=('++' | '--' | '+' | '-') expression {
       if ($op.text.equals("++") || $op.text.equals("--"))
           $n = new PreIncrement($ctx, (LValue)$expression.n, $op.text);
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
       $n = new Assignment($ctx, (LValue)$lhs.n, $rhs.n);
   }
   | 'this' {
       $n = new This($ctx);
   }
   ;


print
returns [Print n]
   : '_print' '(' (args+=expression (',' args+=expression)*)? ')' {
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



