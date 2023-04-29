package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.SyntaxException;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;

public sealed interface Node
    permits Expression, Statement, TypeNode, DeclarationItem, ClassNode, Parameter, MainMethodDefinition, MethodDefinition, Import, FieldDefinition {

    default String getNodeDescription() {
        String fullName = getClass().getSimpleName();
        int index = fullName.lastIndexOf('.');
        if (index >= 0)
            return fullName.substring(index + 1);
        return fullName;
    }

    List<? extends Node> children();

    ParserRuleContext ctx();

    void typecheck(SymbolTable symbols) throws SyntaxException;

    default void generateCode(PrintWriter out, SymbolTable symbols) {
        throw new RuntimeException(String.format("Internal compiler error: generateCode() unimplemented for node %s", this));
    }
}
