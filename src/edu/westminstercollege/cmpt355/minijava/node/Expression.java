package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.Type;

public sealed interface Expression extends Node
    permits Assignment,
        BinaryOp,
        BooleanLiteral,
        Cast,
        ConstructorCall,
        DoubleLiteral,
        FieldAccess,
        IntLiteral,
        LValue,
        MethodCall,
        Negate,
        PostIncrement,
        PreIncrement,
        Print,
        StringLiteral {

    Type getType(SymbolTable symbols);
}
