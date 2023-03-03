package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;

public record TypeNode(String type) implements Node{

    public String getNodeDescription() {
        return String.format("TypeNode [type: %s]", type);
    }
    public List<? extends Node> children() {
        return null;
    }


}