package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;

public record Parameter(ParserRuleContext ctx, TypeNode type, String name) implements Node {

    public Type getType(SymbolTable symbols) {
        return type.type();
    }

    @Override
    public String getNodeDescription() {
        return name;
    }

    @Override
    public List<? extends Node> children() {
        return List.of(type);
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        Variable parameterVar = symbols.findVariable(name).get();
        parameterVar.setType(type.type());
        parameterVar.setIndex(symbols.getVariableCount());
        if (type.type().equals(PrimitiveType.Double)) {
            symbols.allocateLocalVariable(2);
        } else {
            symbols.allocateLocalVariable(1);
        }
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {

    }
}
