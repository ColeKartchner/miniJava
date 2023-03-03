package edu.westminstercollege.cmpt355.minijava.node;

public sealed interface Expression extends Node
        permits IntLiteral, DoubleLiteral, BooleanLiteral, StringLiteral, VariableAccess,
        Assignment, BinaryOp, Negate, PreIncrement, PostIncrement, Cast, Print
        {

}
