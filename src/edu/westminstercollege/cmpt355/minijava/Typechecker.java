package edu.westminstercollege.cmpt355.minijava;


import edu.westminstercollege.cmpt355.minijava.node.*;
import org.antlr.v4.runtime.ParserRuleContext;


import java.util.List;


public class Typechecker {


    public void typecheck(SymbolTable symbols, Node node) throws SyntaxException {
        switch (node) {
            case Block(ParserRuleContext ctx, List<Statement> statements) -> {
                for (var stmt : statements) {
                    typecheck(symbols, stmt);
                }
            }


            case ExpressionStatement(ParserRuleContext ctx, Expression expression) -> {
                typecheck(symbols, expression);
                Type exprType = getType(symbols, expression);
                System.out.println("Expression: " + expression + " has type: " + exprType);
            }


            case Cast(ParserRuleContext ctx, TypeNode targetType, Expression expression) -> {
                var expressionType = getType(symbols, expression);
                var castType = targetType.type();
                var stringType = new ClassType("String");
                // Check for a void value
                if (expressionType.equals(VoidType.Instance)) {
                    throw new SyntaxException(node, "A cast cannot be void");
                    // Check for invalid cast types
                } else if (!castType.equals(stringType) && !(castType instanceof PrimitiveType)) {
                    throw new SyntaxException(node, castType + " is not a valid cast type.");
                    // Check for invalid string casts
                } else if ((castType instanceof PrimitiveType) && expressionType.equals(stringType)) {
                    throw new SyntaxException(node, castType + " cannot be cast to"  + expressionType);
                    // Check for invalid casts to boolean
                } else if ((castType.equals(PrimitiveType.Int) || castType.equals(PrimitiveType.Double))
                        && expressionType.equals(PrimitiveType.Boolean)) {
                    throw new SyntaxException(node, castType + " cannot be cast to " + expressionType);
                }
            }


            case Print(ParserRuleContext ctx, List<Expression> arguments) -> {
                for (var expression : arguments){
                    if(!(getType(symbols, expression) instanceof VoidType)){
                        typecheck(symbols, expression);
                    } else {
                        throw new SyntaxException(node, "Cannot print a void value.");
                    }
                }
            }


            case VariableDeclarations(ParserRuleContext ctx, TypeNode type, List<DeclarationItem> items) -> {
                for (var item : items) {
                    var variable = symbols.findVariable(item.name());
                    // If the variable exists, set its type to the type of the VariableDeclarations
                    if (variable.isPresent()){
                        var realVar = variable.get();
                        realVar.setType(type.type());
                    }
                    // If the DeclarationItem has an initializer, check whether its type matches the type of the variable
                    if (item.children().size() != 0 ) {
                        // If the type of the VariableDeclarations is Double, check whether the initializer is either Double or Int
                        if (type.type().equals(PrimitiveType.Double)){
                            if (!getType(symbols, item.initializer().get()).equals(PrimitiveType.Double)
                                    && !getType(symbols, item.initializer().get()).equals(PrimitiveType.Int)){
                                throw new SyntaxException(node, String.format("Cannot initialize variable: %s cannot be %s", item.name(), getType(symbols, item.initializer().get()).toString()));
                            }
                        }
                        // If the type of the VariableDeclarations is not Double, check whether the initializer type matches the type of the VariableDeclarations
                        else if (!getType(symbols, item.initializer().get()).equals(type.type())){
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


                boolean hasVoid = leftType.equals(VoidType.Instance) || rightType.equals(VoidType.Instance);
                var stringType = new ClassType("String");
                boolean hasNoStrings = !leftType.equals(stringType) && !rightType.equals(stringType);
                boolean hasNonNumeric = (!leftType.equals(PrimitiveType.Int) && !leftType.equals(PrimitiveType.Double))
                        || (!rightType.equals(PrimitiveType.Int) && !rightType.equals(PrimitiveType.Double));


                if(op.equals("+")){
                    if(hasVoid) {
                        throw new SyntaxException(node, "Cannot perform addition with a void value.");
                    } else if (hasNonNumeric && hasNoStrings) {
                        throw new SyntaxException(node, String.format("Addition involving %s and %s not possible.", leftType, rightType));
                    }
                }
                else {
                    if(hasVoid) {
                        throw new SyntaxException(node, "Cannot perform binary operation with a void value.");
                    } else if (hasNonNumeric) {
                        throw new SyntaxException(node, String.format("%s %s %s not possible.", leftType, op, rightType));
                    }
                }
            }


            case Assignment(ParserRuleContext ctx, Expression target, Expression value) -> {
                typecheck(symbols, target);
                typecheck(symbols, value);


                Type leftType = getType(symbols, target),
                        rightType = getType(symbols, value);


                // Check whether the types match, unless the left type is Double and the right type is Int (which can be implicitly cast)
                if (!leftType.equals(rightType)
                        && (!leftType.equals(PrimitiveType.Double) || !rightType.equals(PrimitiveType.Int))) {
                    throw new SyntaxException(node, String.format("Error in assignment. Type: %s cannot be assigned to Type: %s", leftType, rightType));
                }
            }


            case Negate(ParserRuleContext ctx, Expression expression) -> {
                typecheck(symbols, expression);


                var type = getType(symbols, expression);


                // Check whether the type is numerical (either int or double)
                if (!type.equals(PrimitiveType.Int) && !type.equals(PrimitiveType.Double)){
                    throw new SyntaxException(node, "Cannot negate a nonnumerical data type");
                }
            }


            case PreIncrement(ParserRuleContext ctx, Expression target, String op) -> {
                typecheck(symbols, target);


                var type = getType(symbols, target);


                // Check whether the type is numerical (either int or double)
                if (!type.equals(PrimitiveType.Int) && !type.equals(PrimitiveType.Double)){
                    throw new SyntaxException(node, "PreIncrement cannot be invoked on a non-numeric data type: " + type);
                }
            }


            case PostIncrement(ParserRuleContext ctx, Expression target, String op) -> {
                typecheck(symbols, target);


                var type = getType(symbols, target);


                // Check whether the type is numerical (either int or double)
                if (!type.equals(PrimitiveType.Int) && !type.equals(PrimitiveType.Double)){
                    throw new SyntaxException(node, "PostIncrement cannot be invoked on a non-numeric data type: " + type);
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
                var variable = symbols.findVariable(variableName);


                // If the variable is found, return its type
                if (variable.isPresent()) {
                    var varType = variable.get().getType();
                    return varType;
                }
            }


            case Assignment(ParserRuleContext ctx, Expression target, Expression value) -> {
                return getType(symbols, target);
            }


            case BinaryOp(ParserRuleContext ctx, Expression left, Expression right, String op) -> {
                // getType() should always be called *after* typecheck(), so at this
                // point we know that the children have already passed their checks.
                // We are free to use getType() to get their types.


                Type leftType = getType(symbols, left),
                        rightType = getType(symbols, right);


                // Write code that returns the appropriate type based on op, leftType,
                // and rightType


                if (op.equals("+") || op.equals("-")) {


                    // Check if both expressions are numerical types or strings
                    boolean isNumericalOrString = (leftType.equals(PrimitiveType.Int) || leftType.equals(PrimitiveType.Double) || leftType instanceof ClassType)
                            && (rightType.equals(PrimitiveType.Int) || rightType.equals(PrimitiveType.Double) || rightType instanceof ClassType);


                    // Check if at least one of the expressions is a double
                    boolean hasDouble = leftType.equals(PrimitiveType.Double) || rightType.equals(PrimitiveType.Double);


                    if (isNumericalOrString) {
                        if (leftType instanceof ClassType && ((ClassType) leftType).className().equals("String") ||
                                rightType instanceof ClassType && ((ClassType) rightType).className().equals("String")) {
                            return new ClassType("String");
                        } else {
                            if (hasDouble) {
                                return PrimitiveType.Double;
                            } else {
                                return PrimitiveType.Int;
                            }
                        }
                    } else if (leftType.equals(VoidType.Instance) || rightType.equals(VoidType.Instance)) {
                        return VoidType.Instance;
                    } else if (leftType instanceof ClassType || rightType instanceof ClassType) {
                        if (rightType instanceof ClassType && ((ClassType) rightType).className().equals("String")) {
                            return rightType;
                        } else {
                            return leftType;
                        }
                    }
                } else if (op.equals("*") || op.equals("/") || op.equals("%")) {


                    // Check if both expressions are numerical types
                    boolean isNumerical = (leftType.equals(PrimitiveType.Int) || leftType.equals(PrimitiveType.Double))
                            && (rightType.equals(PrimitiveType.Int) || rightType.equals(PrimitiveType.Double));


                    if (isNumerical) {
                        if (leftType.equals(PrimitiveType.Double) || rightType.equals(PrimitiveType.Double)) {
                            return PrimitiveType.Double;
                        } else {
                            return PrimitiveType.Int;
                        }
                    } else if (leftType instanceof ClassType || rightType instanceof ClassType) {
                        if (rightType instanceof ClassType) {
                            return rightType;
                        } else {
                            return leftType;
                        }
                    }
                }
            }


            case Negate(ParserRuleContext ctx, Expression expression) -> {
                return getType(symbols, expression);
            }


            case PreIncrement(ParserRuleContext ctx, Expression target, String op) -> {
                return getType(symbols, target);
            }


            case PostIncrement(ParserRuleContext ctx, Expression target, String op) -> {
                return getType(symbols, target);
            }


            case Cast(ParserRuleContext ignored, TypeNode targetType, Expression expression) -> {
                return targetType.type();
            }


            case Print(ParserRuleContext ctx, List<Expression> arguments) -> {
                return VoidType.Instance;
            }
        }
        return null;
    }
}



