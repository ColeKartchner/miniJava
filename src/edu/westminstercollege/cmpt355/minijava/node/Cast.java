package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;

public record Cast(TypeNode left, Expression right) implements Expression{
    @Override
    public List<Object> children() {
        return List.of(left, right);
    }

    @Override
    public String getNodeDescription() {
        return Expression.super.getNodeDescription();
    }
}
