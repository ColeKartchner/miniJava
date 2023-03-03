package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;

public record Block(List<Statement> statements) implements Statement{
    @Override
    public List<Object> children() {
        return statements;
    }

    public String getNodeDescription() {
        return Statement.super.getNodeDescription();
    }

}
