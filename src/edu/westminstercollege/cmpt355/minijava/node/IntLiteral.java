package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;

public record IntLiteral(String text) implements Expression{
    @Override
    public List<Object> children() {
        return List.of();
    }

    @Override
    public String getNodeDescription() {
        return String.format("IntLiteral [op: %s]", text);
    }
}
