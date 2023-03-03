package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;

public record DoubleLiteral(String text) implements Expression{
    @Override
    public List<Object> children() {
        return List.of();
    }

    @Override
    public String getNodeDescription() {
        return String.format("DoubleLiteral [op: %s]", text);
    }
}
