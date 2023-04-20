package edu.westminstercollege.cmpt355.minijava.node;


public sealed interface LValue extends Expression
        permits VariableAccess {
}
