package edu.westminstercollege.cmpt355.minijava.node;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public record Block(ParserRuleContext ctx, List<Statement> statements) implements Statement {

    @Override
    public List<? extends Node> children() {
        return statements;
    }
}
