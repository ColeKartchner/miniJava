package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;

public record ExpressionStatement(Expression expressions) implements Statement{
    @Override
    public List<Object> children() {
        return List.of(expressions);
    }

    @Override
    public String getNodeDescription() {
        return Statement.super.getNodeDescription();
    }
}
