package edu.westminstercollege.cmpt355.minijava.node;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public record Assignment(ParserRuleContext ctx, Expression target, Expression value) implements Expression {

    @Override
    public List<? extends Node> children() {
        return List.of(target, value);
    }
}
