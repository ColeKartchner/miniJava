package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.node.MethodDefinition;
import edu.westminstercollege.cmpt355.minijava.node.Node;
import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.SyntaxException;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;

/*
    classNodes
returns [ClassNode n]
    : (methods+=method)* EOF {
        var methos = new ArrayList<MethodBody>();
        for (var method : $methods)
            methos.add(method.n);
        $n = new ClassNode($ctx, methos);
    }
    ;

method
    : NAME '(' (args+=expr (',' args+=expr )*)? ')' block {
        var arguments = new ArrayList<Expression>();
        for (var arg : $args)
            arguments.add(arg.n);
        $n = new MethodDefinition($ctx, $NAME.text, arguments, $block.n);
    }
    ;
 */

public record ClassNode(ParserRuleContext ctx, List<MethodDefinition> methods) implements Node {
    @Override
    public List<? extends Node> children() {
        return methods;
    }

    @Override
    public ParserRuleContext ctx() {
        return ctx;
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {

    }
}
