package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.PrimitiveType;
import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.SyntaxException;
import edu.westminstercollege.cmpt355.minijava.Type;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;

public record Negate(ParserRuleContext ctx, Expression expression) implements Expression {

    @Override
    public List<? extends Node> children() {
        return List.of(expression);
    }

    @Override
    public Type getType(SymbolTable symbols) {
        return expression().getType(symbols);
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        expression.typecheck(symbols);
        var type = expression.getType(symbols);
        if (type != PrimitiveType.Int && type != PrimitiveType.Double)
            throw new SyntaxException(String.format("Invalid type for negation: - %s", type));
    }

    public void generateCode(PrintWriter out, SymbolTable symbols) {
        // Have child generate its code. Once this is done, the value to
        // negate should be on top of the stack.
        expression.generateCode(out, symbols);

        var type = expression.getType(symbols);

        // For a negate node, the only possibilities should be integer
        // or double. (If the child is any other type, then typecheck()
        // would have thrown an exception.)
        // However, to make code easier to debug, it is recommended that
        // you do like the below, which throws an exception if it was an
        // unexpected type.
        if (type == PrimitiveType.Int)
            out.println("ineg");
        else if (type == PrimitiveType.Double)
            out.println("dneg");
        else
            throw new RuntimeException(String.format(
                    "Internal compiler error: type of negate is %s", type
            ));
    }
}
