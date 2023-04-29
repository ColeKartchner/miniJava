package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.PrimitiveType;
import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.SyntaxException;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;

public record If(ParserRuleContext ctx, Expression condition, Statement body) implements Statement {
    @Override
    public List<? extends Node> children() {
        return List.of(condition, body);
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        condition.typecheck(symbols);
        body.typecheck(symbols);

        var conditionType = condition.getType(symbols);
        if (conditionType != PrimitiveType.Boolean)
            throw new SyntaxException(this, "The condition of an if must be boolean, not " + conditionType);
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        /*String IfEndLabel = symbols.newLabel ("if_end");

        condition.generateCode(out, symbols);
        // My condition is boolean, so the top of the stack is either true (1) or false (0).
        // If it's true, execute body
        // if false go to end of the loop
        out.printf("ifeq %s:\n", IfEndLabel);

        body.generateCode(out, symbols);

        out.printf("%s:\n", IfEndLabel);

         */
    }
}
