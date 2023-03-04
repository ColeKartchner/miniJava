package edu.westminstercollege.cmpt355.minijava.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record VarDeclarations(TypeNode left, List<VarDeclaration> right) implements Statement{
    public List<? extends Node> children() {
        List<Node> vlist = new ArrayList<>();
        vlist.add(left);
        for (var element : right) {
            vlist.add(element);
        }
        return vlist;
    }
}
