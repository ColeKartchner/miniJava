package edu.westminstercollege.cmpt355.minijava.node;

public sealed interface Expression extends Node
    permits Assignment,
        BinaryOp,
        BooleanLiteral,
        Cast,
        DoubleLiteral,
        IntLiteral,
        Negate,
        PostIncrement,
        PreIncrement,
        Print,
        StringLiteral,
        VariableAccess,
        ConstructorCall,
        FieldAccess,
        MethodCall{
}
