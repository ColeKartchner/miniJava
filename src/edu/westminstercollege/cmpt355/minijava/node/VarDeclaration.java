package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;
import java.util.Optional;

public record VarDeclaration(Optional<Expression> o, String text) implements Node{
    public String getNodeDescription() {
        return String.format("VarDeclaration [Name: %s]", text);
    }
    public List<? extends Node> children() {
        if(o.isPresent()) {
            return List.of(o.get());
        }
        else
            return List.of();
    }
}
