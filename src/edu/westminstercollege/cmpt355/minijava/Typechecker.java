package edu.westminstercollege.cmpt355.minijava;

import edu.westminstercollege.cmpt355.minijava.node.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public class Typechecker {

    public void typecheck(SymbolTable symbols, Node node) throws SyntaxException {
        switch (node) {
            case Block(ParserRuleContext ctx, List<Statement> statements) -> {

            }
            case ExpressionStatement(ParserRuleContext ctx, Expression expression) -> {

            }
            case Cast(ParserRuleContext ctx, TypeNode targetType, Expression expression) -> {

            }
            case Print(ParserRuleContext ctx, List<Expression> arguments) -> {

            }
            case VariableDeclarations(ParserRuleContext ctx, TypeNode type, List<DeclarationItem> items) -> {

            }
            case VariableAccess(ParserRuleContext ctx, String variableName) -> {

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

            }
            case Assignment(ParserRuleContext ctx, Expression target, Expression value) -> {

            }
            case Negate(ParserRuleContext ctx, Expression expression) -> {

            }
            case PreIncrement(ParserRuleContext ctx, Expression target, String op) -> {

            }
            case PostIncrement(ParserRuleContext ctx, Expression target, String op) -> {

            }
            default -> {}
        }
    }

    public Type getType(SymbolTable symbols, Expression expr) {
        switch(expr) {
            case IntLiteral(ParserRuleContext ctx, String text) -> {

            }
            case DoubleLiteral(ParserRuleContext ctx, String text) -> {

            }
            case BooleanLiteral(ParserRuleContext ctx, String text) -> {

            }
            case StringLiteral(ParserRuleContext ctx, String text) -> {

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