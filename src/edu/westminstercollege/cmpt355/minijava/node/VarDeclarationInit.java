package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;

public record VarDeclarationInit(Expression expression, String text) {

    public List<? extends Node> children() {
        return List.of(expression);
    }

    public String getNodeDescription() {
        return String.format("VarDeclarationInit [Name: %s]", text);
    }
}
