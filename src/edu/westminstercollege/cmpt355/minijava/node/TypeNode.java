package edu.westminstercollege.cmpt355.minijava.node;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public record TypeNode(ParserRuleContext ctx, String type) implements Node {

    @Override
    public String getNodeDescription() {
        return String.format("TypeNode [type: %s]", type);
    }

    @Override
    public List<? extends Node> children() {
        return List.of();
    }
}
