package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;

public record VariableAccess(ParserRuleContext ctx, String variableName) implements LValue {

    @Override
    public String getNodeDescription() {
        return String.format("VariableAccess [variableName: %s]", variableName);
    }

    @Override
    public List<? extends Node> children() {
        return List.of();
    }

    @Override
    public Type getType(SymbolTable symbols) {
        var maybeVariable = symbols.findVariable(variableName);
        if (maybeVariable.isPresent())
            return symbols.findVariable(variableName).get().getType();

        var maybeClass = symbols.findJavaClass(variableName);
        if (maybeClass.isPresent())
            return new StaticType(maybeClass.get().getName());
        else
            throw new RuntimeException(String.format("Internal compiler error: can't find variable/class named %s", variableName));
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        // do nothing
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        var type = getType(symbols);
        if (type instanceof StaticType)
            return;

        var var = symbols.findVariable(variableName).get();

        if (type == PrimitiveType.Int || type == PrimitiveType.Boolean)
            out.printf("iload %d\n", var.getIndex());
        else if (type == PrimitiveType.Double)
            out.printf("dload %d\n", var.getIndex());
        else if (type instanceof ClassType)
            out.printf("aload %d\n", var.getIndex());
        else if (type instanceof StaticType)
            ; // do nothing
        else
            throw new RuntimeException(String.format("Access to variable of type %s", type));
    }
}
