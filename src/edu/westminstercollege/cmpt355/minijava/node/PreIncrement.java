package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.PrimitiveType;
import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.SyntaxException;
import edu.westminstercollege.cmpt355.minijava.Type;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;

public record PreIncrement(ParserRuleContext ctx, LValue target, String op) implements Expression {

    @Override
    public String getNodeDescription() {
        return String.format("PreIncrement [op: %s]", op);
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
        var type = target.getType(symbols);
        if (type != PrimitiveType.Int && type != PrimitiveType.Double)
            throw new SyntaxException(String.format("Invalid type in increment: %s %s", op, type));
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        if (target instanceof VariableAccess va) {
            int varIndex = symbols.findVariable(va.variableName()).get().getIndex();
            if (va.getType(symbols) == PrimitiveType.Int) {
                out.printf("iinc %d %d\n", varIndex, op.equals("++") ? 1 : -1);
                out.printf("iload %d\n", varIndex);
            } else if (va.getType(symbols) == PrimitiveType.Double) {
                out.printf("dload %d\n", varIndex);
                out.println("dconst_1");
                out.println(op.equals("++") ? "dadd" : "dsub");
                out.println("dup2");
                out.printf("dstore %d\n", varIndex);
            } else
                throw new RuntimeException(String.format("Internal compiler error: preincrement on variable of type %s", va.getType(symbols)));
        } else
            throw new RuntimeException(String.format("Internal compiler error: preincrement unimplemented on node %s", target));
    }
}
