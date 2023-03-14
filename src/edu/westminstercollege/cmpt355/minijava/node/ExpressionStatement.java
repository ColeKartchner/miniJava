package edu.westminstercollege.cmpt355.minijava.node;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public record ExpressionStatement(ParserRuleContext ctx, Expression expression) implements Statement {

    @Override
    public List<? extends Node> children() {
        return List.of(expression);
    }
}
