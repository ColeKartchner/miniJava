package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;

public record StringLiteral(ParserRuleContext ctx, String text) implements Expression {

    @Override
    public String getNodeDescription() {
        return String.format("StringLiteral [text: %s]", text);
    }

    @Override
    public List<? extends Node> children() {
        return List.of();
    }

    @Override
    public Type getType(SymbolTable symbols) {
        return new ClassType("String");
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        // do nothing
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        out.printf("ldc %s\n", text);
    }
}
