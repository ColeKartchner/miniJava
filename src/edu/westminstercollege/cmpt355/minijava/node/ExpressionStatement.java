package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.PrimitiveType;
import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.SyntaxException;
import edu.westminstercollege.cmpt355.minijava.VoidType;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;

public record ExpressionStatement(ParserRuleContext ctx, Expression expression) implements Statement {

    @Override
    public List<? extends Node> children() {
        return List.of(expression);
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        expression.typecheck(symbols);
        System.out.printf("ExpressionStatement: type is %s\n", expression.getType(symbols));
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        var type = expression.getType(symbols);

        expression.generateCode(out, symbols);

        if (type == PrimitiveType.Double)
            out.println("pop2");
        else if (type != VoidType.Instance)
            out.println("pop");
    }
}
