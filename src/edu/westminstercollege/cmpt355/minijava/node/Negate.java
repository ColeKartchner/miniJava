package edu.westminstercollege.cmpt355.minijava.node;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public record Negate(ParserRuleContext ctx, Expression expression) implements Expression {

    @Override
    public List<? extends Node> children() {
        return List.of(expression);
    }
}
