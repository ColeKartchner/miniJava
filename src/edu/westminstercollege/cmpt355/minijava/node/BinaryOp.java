package edu.westminstercollege.cmpt355.minijava.node;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public record BinaryOp(ParserRuleContext ctx, Expression left, Expression right, String op) implements Expression {

    @Override
    public String getNodeDescription() {
        return String.format("BinaryOp [op: %s]", op);
    }

    @Override
    public List<? extends Node> children() {
        return List.of(left, right);
    }
}
