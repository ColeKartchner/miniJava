package edu.westminstercollege.cmpt355.minijava.node;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

public record ConstructorCall(ParserRuleContext ctx, String name, List<Expression> args) implements Expression {
    @Override
    public String getNodeDescription() {
        return String.format("ConstructorCall [Constructor: %s]", name);
    }
    @Override
    public List<? extends Node> children() {
        return args;
    }

}
