package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.SyntaxException;
import edu.westminstercollege.cmpt355.minijava.Type;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public record MethodDefinition(ParserRuleContext ctx, TypeNode returnType, String name, List<Parameter> parameters, Block block) implements Node {
    @Override
    public String getNodeDescription() {
        return "name";
    }

    public List<? extends Node> children() {
        List<Node> list = new ArrayList<>();
        list.add(returnType);
        list.addAll(parameters);
        list.add(block);
        return list;
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {}

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {

    }

    public TypeNode getReturnType() {
        return returnType;
    }
}
