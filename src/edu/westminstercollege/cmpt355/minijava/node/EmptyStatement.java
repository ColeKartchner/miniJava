package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;

public record EmptyStatement() implements Statement{
    @Override
    public List<? extends Node> children() {
        return List.of();
    }

    @Override
    public String getNodeDescription() {
        return String.format("Empty Statement");
    }
}
