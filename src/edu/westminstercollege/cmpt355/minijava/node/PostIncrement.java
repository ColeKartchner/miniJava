package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

public record PostIncrement(ParserRuleContext ctx, LValue target, String op) implements Expression {

    @Override
    public String getNodeDescription() {
        return String.format("PostIncrement [op: %s]", op);
    }

    @Override
    public List<? extends Node> children() {
        return List.of(target);
    }

    @Override
    public Type getType(SymbolTable symbols) {
        return target.getType(symbols);
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        target.typecheck(symbols);
        var targetType = target.getType(symbols);
        if (targetType != PrimitiveType.Int && targetType != PrimitiveType.Double)
            throw new SyntaxException(String.format("Invalid type for increment: %s %s", targetType, op));
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        if (target instanceof VariableAccess va) {
            int varIndex = symbols.findVariable(va.variableName()).get().getIndex();
            if (va.getType(symbols) == PrimitiveType.Int) {
                out.printf("iload %d\n", varIndex);
                out.printf("iinc %d %d\n", varIndex, op.equals("++") ? 1 : -1);
            } else if (va.getType(symbols) == PrimitiveType.Double) {
                out.printf("dload %d\n", varIndex);
                out.println("dup2");
                out.println("dconst_1");
                out.println(op.equals("++") ? "dadd" : "dsub");
                out.printf("dstore %d\n", varIndex);
            } else
                throw new RuntimeException(String.format("Internal compiler error: postincrement on variable of type %s", va.getType(symbols)));
        } else
            throw new RuntimeException(String.format("Internal compiler error: postincrement unimplemented on node %s", target));
    }
}
