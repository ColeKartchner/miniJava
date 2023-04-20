package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.SyntaxException;
import edu.westminstercollege.cmpt355.minijava.node.Node;
import edu.westminstercollege.cmpt355.minijava.node.Expression;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

public record DeclarationItem(ParserRuleContext ctx, String name, Optional<Expression> initializer) implements Node {

    @Override
    public String getNodeDescription() {
        return String.format("DeclarationItem [name: %s]", name);
    }

    @Override
    public List<? extends Node> children() {
        return initializer.stream().toList();
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        // do nothing
        // (Typechecking of initializer is done in VariableDeclarations)
    }
}
