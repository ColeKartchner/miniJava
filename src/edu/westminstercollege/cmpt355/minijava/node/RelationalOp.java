package edu.westminstercollege.cmpt355.minijava.node;

import com.sun.jdi.ClassType;
import edu.westminstercollege.cmpt355.minijava.PrimitiveType;
import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.SyntaxException;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;

public record RelationalOp(ParserRuleContext ctx, Expression left, Expression right, String op) implements Expression {

    @Override
    public List<? extends Node> children() {
        return List.of(left, right);
    }
    @Override
    public String getNodeDescription() {
        return String.format("RelationalOp [op: %s]", op);
    }

    public type getType (SymbolTable symbols) {
        return PrimitiveType.Boolean;
    }

    public void typecheck (SymbolTable symbols) throws SyntaxException {
        // <, <=, >, >= : Numeric types only
        // ==, != : any (non-void) type, both must be same category
        //          (numeric, boolean, or object)
        left.typecheck(symbols);
        right.typecheck(symbols);

        var leftType = left.getType(symbols);
        var rightType = right.getType(symbols);
        boolean bothNumeric = isNumeric(leftType) && isNumeric(rightType);
        boolean bothBoolean = leftType == PrimitiveType.Boolean && rightType == PrimitiveType.Boolean;
        boolean bothObject = leftType instanceof ClassType && rightType instanceof ClassType;
        SyntaxException ex = new SyntaxException(this, String.format("Incompatible types: %s, %s, %s", leftType, op, rightType));


        if (List.of("<", "<=", ">", ">=").contains(op)) {
            if (!bothNumeric) {
                throw ex;
            }
        } else if (!(bothNumeric || bothBoolean || bothObject))
                throw ex;
        }

        condition.typecheck(symbols);
        body.typcheck(symbols);

        Type conditionType = condition.getType(symbols);
        if (conditionType != PrimitiveType.Boolean)){
            throw new SyntaxException(node, "While condition must be boolean, not %s", conditionType);
        }
    }
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        out.println("loop_start:");
        condition.generateCode(out, symbols);
        // My condition is boolean, so the top of the stack is either true (1) or false (0).
        // If it's true, execute body
        // if false go to end of the loop
        out.println("ifeq loop_end");

        body.generateCode(out, symbols);

        out.println("goto loop_start");

        out.println("loop_end:");
    }

    private boolean isNumeric(Type type) {
        return type == PrimitiveType.Int || type == PrimitiveType.Double;
    }
}
