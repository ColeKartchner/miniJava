package edu.westminstercollege.cmpt355.minijava.node;

import com.sun.jdi.ClassType;
import edu.westminstercollege.cmpt355.minijava.PrimitiveType;
import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.SyntaxException;
import edu.westminstercollege.cmpt355.minijava.Type;
import org.antlr.v4.runtime.ParserRuleContext;


import java.io.PrintWriter;
import java.util.List;

public record While(ParserRuleContext ctx, Expression condition, Statement body) implements Statement {
    @Override
    public List<? extends Node> children() {
        return List.of(condition, body);
    }

    public void typecheck (SymbolTable symbols) throws SyntaxException {
       /* condition.typecheck(symbols);
        body.typecheck(symbols);

        Type conditionType = condition.getType(symbols);
        if (conditionType != PrimitiveType.Boolean)){
            throw new SyntaxException(node, "While condition must be boolean, not %s", conditionType);
        }

        */
    }
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        /*
        String loopStartLabel = symbols.newLabel("loop_start");
        String loopEndLabel = symbols.newLabel("loop_end");

        //out.printf("%s:\n", loopStartLabel);
        condition.generateCode(out, symbols);
        // My condition is boolean, so the top of the stack is either true (1) or false (0).
        // If it's true, execute body
        // if false go to end of the loop
        out.printf("ifeq %s:\n", loopEndLabel);

        body.generateCode(out, symbols);

        out.printf("goto %s:\n", loopStartLabel);

        out.printf("%s:\n", loopEndLabel);

         */
    }


}