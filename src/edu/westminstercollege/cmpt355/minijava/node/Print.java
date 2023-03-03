package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;

public record Print(Expression expressions) implements Expression{
    @Override
    public List<? extends Node> children() {
        return List.of(expressions);
    }

    public String getNodeDescription() {
        return Expression.super.getNodeDescription();
    }
}
