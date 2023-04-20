package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.PrimitiveType;
import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.SyntaxException;
import edu.westminstercollege.cmpt355.minijava.Type;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;

public record BooleanLiteral(ParserRuleContext ctx, String text) implements Expression {

    @Override
    public String getNodeDescription() {
        return String.format("BooleanLiteral [text: %s]", text);
    }

    @Override
    public List<? extends Node> children() {
        return List.of();
    }

    @Override
    public Type getType(SymbolTable symbols) {
        return PrimitiveType.Boolean;
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        // do nothing
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        if ("true".equals(text))
            out.println("iconst_1");
        else
            out.println("iconst_0");
    }
}
