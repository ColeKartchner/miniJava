package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;

public record Assignment(ParserRuleContext ctx, LValue target, Expression value) implements Expression {

    @Override
    public List<? extends Node> children() {
        return List.of(target, value);
    }

    @Override
    public Type getType(SymbolTable symbols) {
        return target.getType(symbols);
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        target.typecheck(symbols);
        value.typecheck(symbols);

        var lhsType = target.getType(symbols);
        var rhsType = value.getType(symbols);

        if (lhsType == PrimitiveType.Double) {
            if (rhsType != PrimitiveType.Int && rhsType != PrimitiveType.Double)
                throw new SyntaxException(this, String.format("Incompatible types in assignment: double = %s", rhsType));
        }

        else if (!lhsType.equals(rhsType))
            throw new SyntaxException(this, String.format("Incompatible types in assignment: %s = %s", lhsType, rhsType));
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        var lhsType = target.getType(symbols);
        var rhsType = value.getType(symbols);

        if (target instanceof VariableAccess va) {
            var var = symbols.findVariable(va.variableName()).get();

            value.generateCode(out, symbols);

            if (lhsType == PrimitiveType.Double && rhsType == PrimitiveType.Int)
                out.println("i2d");

            if (lhsType == PrimitiveType.Int || lhsType == PrimitiveType.Boolean)
                out.printf("dup\nistore %d\n", var.getIndex());
            else if (lhsType == PrimitiveType.Double)
                out.printf("dup2\ndstore %d\n", var.getIndex());
            else if (lhsType instanceof ClassType)
                out.printf("dup\nastore %d\n", var.getIndex());
            else
                throw new RuntimeException(String.format("Internal compiler error: assignment to variable of type %s", lhsType));
        }
    }
}
