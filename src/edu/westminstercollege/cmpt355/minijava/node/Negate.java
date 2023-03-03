package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;

public record Negate(String left, String right) implements Expression{
    @Override
    public List<? extends Node> children() {
        return List.of(left, right);
    }

    @Override
    public String getNodeDescription() {
        return Expression.super.getNodeDescription();
    }
}