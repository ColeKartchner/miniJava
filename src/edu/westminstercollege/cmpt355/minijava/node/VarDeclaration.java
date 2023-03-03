package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;

public record VarDeclaration(TypeNode left, VarDeclarationInit right, String text) implements Node{
    public String getNodeDescription() {
        return String.format("VarDeclaration [Name: %s]", text);
    }
    public List<? extends Node> children() {
        return (List<? extends Node>) List.of(left, right);
    }
}
