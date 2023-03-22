package edu.westminstercollege.cmpt355.minijava;

import edu.westminstercollege.cmpt355.minijava.node.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public class Typechecker {

    public void typecheck(SymbolTable symbols, Node node) throws SyntaxException {
        switch (node) {
            case Block(ParserRuleContext ctx, List<Statement> statements) -> {
                for (var statement : statements) {
                    typecheck(symbols, statement);
                }
            }
            case ExpressionStatement(ParserRuleContext ctx, Expression expression) -> {
                typecheck(symbols, expression);
                Type expressionType = getType(symbols, expression);
                System.out.println("Type of " + expression + ": " + expressionType.toString());

            }
            case Cast(ParserRuleContext ctx, TypeNode targetType, Expression expression) -> {
                var expressionType = getType(symbols, expression);
                var castType = targetType.type();
                var stringType = new ClassType("String");
                // If a void value
                if (expressionType.equals(VoidType.Instance)) {
                    throw new SyntaxException(node, "A cast cannot be void");
                } else if (!castType.equals(stringType) && !(castType instanceof PrimitiveType)) {
                    throw new SyntaxException(node, castType + " is not a valid cast type.");
                    // Catches the types that cannot be cast to a string
                } else if ((castType instanceof PrimitiveType) && expressionType.equals(stringType)) {
                    throw new SyntaxException(node, castType + " cannot be cast to String");
                    // ints/doubles cannot be cast to boolean
                } else if ((castType.equals(PrimitiveType.Int) || castType.equals(PrimitiveType.Double))
                        && expressionType.equals(PrimitiveType.Boolean)) {
                    throw new SyntaxException(node, castType + " cannot be cast to " + expressionType);
                }

            }
            case Print(ParserRuleContext ctx, List<Expression> arguments) -> {
                for(var expression : arguments){
                    typecheck(symbols, expression);
                    if(getType(symbols, expression) instanceof VoidType){
                        throw new SyntaxException(node, "Cannot print a void value.");
                    }
                }

            }
            case VariableDeclarations(ParserRuleContext ctx, TypeNode type, List<DeclarationItem> items) -> {
                for(var item : items) {
                    var variable = symbols.findVariable(item.name());
                    if(variable.isPresent()){
                        var realVar = variable.get();
                        realVar.setType(type.type());
                    }
                    if(item.children().size() != 0 ) {
                        if(type.type().equals(PrimitiveType.Double)){
                            if(!getType(symbols, item.initializer().get()).equals(PrimitiveType.Double)
                                    && !getType(symbols, item.initializer().get()).equals(PrimitiveType.Int)){
                                throw new SyntaxException(node, String.format("Cannot initialize variable: %s cannot be %s", item.name(), getType(symbols, item.initializer().get()).toString()));
                            }
                        }
                        else if(!getType(symbols, item.initializer().get()).equals(type.type())){
                            throw new SyntaxException(node, String.format("Cannot initialize variable: %s cannot be %s", item.name(), getType(symbols, item.initializer().get()).toString()));
                        }
                    }
                }

            }
            case VariableAccess(ParserRuleContext ctx, String variableName) -> {
                var variable = symbols.findVariable(variableName);
                if (variable.isEmpty()){
                    throw new SyntaxException(node, String.format("Variable '%s' is undefined. Please declare it before accessing it.", variableName));
                }
            }
            case BinaryOp(ParserRuleContext ctx, Expression left, Expression right, String op) -> {
                // Make sure the children pass their checks
                typecheck(symbols, left);
                typecheck(symbols, right);

                // Now that they have passed, we can use their types
                Type leftType = getType(symbols, left),
                        rightType = getType(symbols, right);

                // Insert code that throws a SyntaxException if leftType and rightType
                // don't make sense for this operation (will depend on the value of op!)

                //addition
                //int left
                if(leftType.equals(PrimitiveType.Int) && rightType.equals(VoidType.Instance) && op.equals("+")){
                    throw new SyntaxException(node, String.format("Cannot add Void type."));
                }
                else if(leftType.equals(PrimitiveType.Int) && rightType.equals(ClassType.class) && op.equals("+")){
                    throw new SyntaxException(node, String.format("Cannot add int and class types."));
                }
                else if(leftType.equals(PrimitiveType.Int) && rightType.equals(PrimitiveType.Boolean) && op.equals("+")){
                    throw new SyntaxException(node, String.format("Cannot add boolean type."));
                }
                //double left
                else if(leftType.equals(PrimitiveType.Double) && rightType.equals(VoidType.Instance) && op.equals("+")){
                    throw new SyntaxException(node, String.format("Cannot add Void type."));
                }
                else if(leftType.equals(PrimitiveType.Double) && rightType.equals(ClassType.class) && op.equals("+")){
                    throw new SyntaxException(node, String.format("Cannot add double and class types."));
                }
                else if(leftType.equals(PrimitiveType.Double) && rightType.equals(PrimitiveType.Boolean) && op.equals("+")) {
                    throw new SyntaxException(node, String.format("Cannot add boolean type."));
                }
                //Class left
                else if(leftType.equals(ClassType.class) && rightType.equals(VoidType.Instance) && op.equals("+")){
                    throw new SyntaxException(node, String.format("Cannot add Void type."));
                }
                else if(leftType.equals(ClassType.class) && rightType.equals(PrimitiveType.Boolean) && op.equals("+")){
                    throw new SyntaxException(node, String.format("Cannot add Void type."));
                }
                else if(leftType.equals(VoidType.Instance) && rightType.equals(PrimitiveType.Int) && op.equals("+")){
                    throw new SyntaxException(node, String.format("Cannot add Void type."));
                }
                else if(leftType.equals(VoidType.Instance) && rightType.equals(PrimitiveType.Double) && op.equals("+")){
                    throw new SyntaxException(node, String.format("Cannot add Void type."));
                }
                else if(leftType.equals(VoidType.Instance) && rightType.equals(PrimitiveType.Boolean) && op.equals("+")){
                    throw new SyntaxException(node, String.format("Cannot add Void or Boolean type."));
                }
                else if(leftType.equals(VoidType.Instance) && rightType.equals(VoidType.Instance) && op.equals("+")){
                    throw new SyntaxException(node, String.format("Cannot add Void type."));
                }
                else if(leftType.equals(VoidType.Instance) && rightType.equals(ClassType.class) && op.equals("+")){
                    throw new SyntaxException(node, String.format("Cannot add Void type."));
                }
                //Boolean left
                else if(leftType.equals(PrimitiveType.Boolean) && rightType.equals(VoidType.Instance) && op.equals("+")){
                    throw new SyntaxException(node, String.format("Cannot add Void type."));
                }
                else if(leftType.equals(PrimitiveType.Boolean) && rightType.equals(ClassType.class) && op.equals("+")){
                    throw new SyntaxException(node, String.format("Cannot add double and class types."));
                }
                else if(leftType.equals(PrimitiveType.Boolean) && rightType.equals(PrimitiveType.Boolean) && op.equals("+")) {
                    throw new SyntaxException(node, String.format("Cannot add boolean type."));
                }

                //subtraction
                if(!leftType.equals(PrimitiveType.Double) || !rightType.equals(PrimitiveType.Double) && op.equals("-")){
                    throw new SyntaxException(node, String.format("Subtract cannot be invoked on a non-numeric data type"));
                }
                if(!leftType.equals(PrimitiveType.Double) || !rightType.equals(PrimitiveType.Int) && op.equals("-")){
                    throw new SyntaxException(node, String.format("Subtract cannot be invoked on a non-numeric data type"));
                }
                if(!leftType.equals(PrimitiveType.Int) || !rightType.equals(PrimitiveType.Int) && op.equals("-")){
                    throw new SyntaxException(node, String.format("Subtract cannot be invoked on a non-numeric data type"));
                }
                if(!leftType.equals(PrimitiveType.Int) || !rightType.equals(PrimitiveType.Double) && op.equals("-")){
                    throw new SyntaxException(node, String.format("Subtract cannot be invoked on a non-numeric data type"));
                }

                //multiplication
                if(!leftType.equals(PrimitiveType.Double) || !rightType.equals(PrimitiveType.Double) && op.equals("-")){
                    throw new SyntaxException(node, String.format("Multiply cannot be invoked on a non-numeric data type"));
                }
                if(!leftType.equals(PrimitiveType.Double) || !rightType.equals(PrimitiveType.Int) && op.equals("-")){
                    throw new SyntaxException(node, String.format("Multiply cannot be invoked on a non-numeric data type"));
                }
                if(!leftType.equals(PrimitiveType.Int) || !rightType.equals(PrimitiveType.Int) && op.equals("-")){
                    throw new SyntaxException(node, String.format("Multiply cannot be invoked on a non-numeric data type"));
                }
                if(!leftType.equals(PrimitiveType.Int) || !rightType.equals(PrimitiveType.Double) && op.equals("-")){
                    throw new SyntaxException(node, String.format("Multiply cannot be invoked on a non-numeric data type"));
                }

                //division
                if(!leftType.equals(PrimitiveType.Double) || !rightType.equals(PrimitiveType.Double) && op.equals("-")){
                    throw new SyntaxException(node, String.format("Divide cannot be invoked on a non-numeric data type"));
                }
                if(!leftType.equals(PrimitiveType.Double) || !rightType.equals(PrimitiveType.Int) && op.equals("-")){
                    throw new SyntaxException(node, String.format("Divide cannot be invoked on a non-numeric data type"));
                }
                if(!leftType.equals(PrimitiveType.Int) || !rightType.equals(PrimitiveType.Int) && op.equals("-")){
                    throw new SyntaxException(node, String.format("Divide cannot be invoked on a non-numeric data type"));
                }
                if(!leftType.equals(PrimitiveType.Int) || !rightType.equals(PrimitiveType.Double) && op.equals("-")){
                    throw new SyntaxException(node, String.format("Divide cannot be invoked on a non-numeric data type"));
                }

                case Assignment(ParserRuleContext ctx, Expression target, Expression value) -> {
                    typecheck(symbols, target);
                    typecheck(symbols, value);
                    Type leftType = getType(symbols, target),
                            rightType = getType(symbols, value);

                    if (!leftType.equals(rightType)
                            && (!leftType.equals(PrimitiveType.Double) || !rightType.equals(PrimitiveType.Int))) {
                        throw new SyntaxException(node, String.format("Error in assignment. Type: %s cannot be assigned to Type: %s", leftType, rightType));
                    }
                }
                case Negate(ParserRuleContext ctx, Expression expression) -> {
                    typecheck(symbols, expression);
                    Type expressionType = getType(symbols, expression);

                    if(!expressionType.equals(PrimitiveType.Double) || !expressionType.equals(PrimitiveType.Int)){
                        throw new SyntaxException(node, String.format("Cannot negate a nonnumerical data type"));
                    }
                }
                case PreIncrement(ParserRuleContext ctx, Expression target, String op) -> {
                    typecheck(symbols, target);
                    Type targetType = getType(symbols, target);

                    if(!targetType.equals(PrimitiveType.Double) || !targetType.equals(PrimitiveType.Int)){
                        throw new SyntaxException(node, String.format("PreIncrement cannot be invoked on a non-numeric data type"));
                    }
                }
                case PostIncrement(ParserRuleContext ctx, Expression target, String op) -> {
                    typecheck(symbols, target);
                    Type targetType = getType(symbols, target);

                    if(!targetType.equals(PrimitiveType.Double) || !targetType.equals(PrimitiveType.Int)){
                        throw new SyntaxException(node, String.format("PostIncrement cannot be invoked on a non-numeric data type"));
                    }
                }
                default -> {}
            }
        }

    public Type getType(SymbolTable symbols, Expression expr) {
        switch(expr) {
            case IntLiteral(ParserRuleContext ctx, String text) -> {
                return PrimitiveType.Int;
            }
            case DoubleLiteral(ParserRuleContext ctx, String text) -> {
                return PrimitiveType.Double;
            }
            case BooleanLiteral(ParserRuleContext ctx, String text) -> {
                return PrimitiveType.Boolean;
            }
            case StringLiteral(ParserRuleContext ctx, String text) -> {
                return new ClassType("String");
            }
            case VariableAccess(ParserRuleContext ctx, String variableName) -> {

            }
            case Assignment(ParserRuleContext ctx, Expression target, Expression value) -> {

            }
            case BinaryOp(ParserRuleContext ctx, Expression left, Expression right, String op) -> {
                // getType() should always be called *after* typecheck(), so at this
                // point we know that the children have already passed their checks.
                // We are free to use getType() to get their types.

                Type leftType = getType(symbols, left),
                        rightType = getType(symbols, right);

                // Write code that returns the appropriate type based on op, leftType,
                // and rightType

            }
            case Negate(ParserRuleContext ctx, Expression expression) -> {

            }
            case PreIncrement(ParserRuleContext ctx, Expression target, String op) -> {

            }
            case PostIncrement(ParserRuleContext ctx, Expression target, String op) -> {

            }
            case Cast(ParserRuleContext ignored, TypeNode targetType, Expression expression) -> {

            }
            case Print(ParserRuleContext ctx, List<Expression> arguments) -> {

            }
        }
        return null;
    }
}