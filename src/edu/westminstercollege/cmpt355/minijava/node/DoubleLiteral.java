package edu.westminstercollege.cmpt355.minijava.node;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public record DoubleLiteral(ParserRuleContext ctx, String text) implements Expression {

    @Override
    public String getNodeDescription() {
        return String.format("DoubleLiteral [text: %s]", text);
    }

    @Override
    public List<? extends Node> children() {
        return List.of();
    }
}
