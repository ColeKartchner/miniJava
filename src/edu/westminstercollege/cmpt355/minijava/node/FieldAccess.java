package edu.westminstercollege.cmpt355.minijava.node;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public record FieldAccess(ParserRuleContext ctx, Expression expr, String fieldName) implements Expression {

    @Override
    public String getNodeDescription() {
        return String.format("FieldAccess [Field: %s]", fieldName);
    }

    @Override
    public List<? extends Node> children() {
        return List.of(expr);
    }
}
