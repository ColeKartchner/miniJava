package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;

public record Cast(ParserRuleContext ctx, TypeNode targetType, Expression expression) implements Expression {

    @Override
    public List<? extends Node> children() {
        return List.of(targetType, expression);
    }

    @Override
    public Type getType(SymbolTable symbols) {
        return targetType().type();
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        targetType.typecheck(symbols);
        expression.typecheck(symbols);

        var targetType = targetType().type();
        var exprType = expression.getType(symbols);

        var ex = new SyntaxException(this, String.format("Invalid types in cast: (%s) %s", targetType, exprType));

        if (exprType == VoidType.Instance)
            throw ex;
        else if (targetType.equals(ClassType.String)) {
            // Any type (except void) can be cast to String

        } else if (targetType instanceof ClassType || exprType instanceof ClassType)
            // Other class types cannot be cast
            throw ex;
        else if (targetType == PrimitiveType.Boolean || exprType == PrimitiveType.Boolean) {
            // Booleans can only be cast to/from boolean
            if (targetType != exprType)
                throw ex;
        }
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        var toType = targetType.type();
        var fromType = expression.getType(symbols);

        expression.generateCode(out, symbols);

        if (toType.equals(fromType))
            // nothing needs to be done 😉
            return;
        else if (toType == PrimitiveType.Double && fromType == PrimitiveType.Int)
            out.println("i2d");
        else if (toType == PrimitiveType.Int && fromType == PrimitiveType.Double)
            out.println("d2i");
        else if (toType.equals(ClassType.String)) {
            if (fromType == PrimitiveType.Int)
                out.println("invokestatic java/lang/String/valueOf(I)Ljava/lang/String;");
            else if (fromType == PrimitiveType.Double)
                out.println("invokestatic java/lang/String/valueOf(D)Ljava/lang/String;");
            else if (fromType == PrimitiveType.Boolean)
                out.println("invokestatic java/lang/String/valueOf(Z)Ljava/lang/String;");
            else if (fromType instanceof ClassType)
                out.println("invokevirtual java/lang/Object/toString()Ljava/lang/String;");
            else
                throw new RuntimeException(String.format("Internal compiler error: no rule to cast type %s to String", fromType));
        } else
            throw new RuntimeException(String.format("Internal compiler error: unimplemented cast (%s) %s", toType, fromType));
    }
}
