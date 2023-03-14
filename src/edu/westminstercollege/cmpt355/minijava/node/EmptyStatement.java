package edu.westminstercollege.cmpt355.minijava.node;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public record EmptyStatement(ParserRuleContext ctx) implements Statement {

    @Override
    public List<? extends Node> children() {
        return List.of();
    }
}
