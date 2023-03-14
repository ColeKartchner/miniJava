package edu.westminstercollege.cmpt355.minijava.node;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public record Print(ParserRuleContext ctx, List<Expression> arguments) implements Expression {

    @Override
    public List<? extends Node> children() {
        return arguments;
    }
}
