package edu.westminstercollege.cmpt355.minijava.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record VarDeclarations(TypeNode left, List<VarDeclaration> right, String text) implements Statement{
    public String getNodeDescription() {
        return String.format("VarDeclaration [Name: %s]", text);
    }
    public List<? extends Node> children() {
        List<Node> vlist = new ArrayList<>();
        vlist.add(left);
        for (var element : right) {
            vlist.add(element);
        }
        return vlist;
    }
}
