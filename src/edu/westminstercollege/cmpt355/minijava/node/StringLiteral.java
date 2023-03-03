package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;

public record StringLiteral(String text) implements Expression{
    @Override
    public List<Object> children() {
        return List.of();
    }

    @Override
    public String getNodeDescription() {
        return String.format("StringLiteral [op: %s]", text);
    }
}
