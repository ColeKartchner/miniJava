package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;

public record Type(String text) implements Node{

    public String getNodeDescription() {
        return String.format("Type [Type: %s]", text);
    }
    public List<? extends Node> children() {
        return List.of();
    }
}
