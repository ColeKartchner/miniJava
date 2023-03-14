package edu.westminstercollege.cmpt355.minijava.node;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public record PreIncrement(ParserRuleContext ctx, Expression target, String op) implements Expression {

    @Override
    public String getNodeDescription() {
        return String.format("PreIncrement [op: %s]", op);
    }

    @Override
    public List<? extends Node> children() {
        return List.of(target);
    }
}
