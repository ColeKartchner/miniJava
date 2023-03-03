package edu.westminstercollege.cmpt355.minijava.node;

import java.util.List;

public record BinaryOp(VariableAccess left, Expression right, String type) implements Expression{
    @Override
    public List<Object> children() {
        return List.of(left, right);
    }

    public String getNodeDescription() {
        return String.format("BinaryOp [op: %s]", type);
    }
}
