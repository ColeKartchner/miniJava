package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;

public record VarDeclarations(TypeNode left, VarDeclarationInit right, String text) {
    public String getNodeDescription() {
        return String.format("VarDeclaration [Name: %s]", text);
    }
    public List<? extends Node> children() {
        return (List<? extends Node>) List.of(left, right);
    }
}
