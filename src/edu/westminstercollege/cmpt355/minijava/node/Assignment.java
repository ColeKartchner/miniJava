package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;

public record Assignment(VariableAccess left, Cast right) implements Expression{
    public String getNodeDescription() {
        return Expression.super.getNodeDescription();
    }

    @Override
    public List<? extends Node> children() {
        return List.of(left, right);
    }
}