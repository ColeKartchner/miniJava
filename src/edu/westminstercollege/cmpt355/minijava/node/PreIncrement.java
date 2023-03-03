package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;

public record PreIncrement(VariableAccess v, String text) implements Expression{
    @Override
    public List<Object> children() {
        return List.of(v);
    }

    public String getNodeDescription() {
        return String.format("PostIncrement [op: %s]", text);
    }
}