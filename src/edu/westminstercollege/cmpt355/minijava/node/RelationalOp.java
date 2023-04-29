package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.*;
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

    @Override
    public Type getType(SymbolTable symbols) {
        return null;
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

        //left..typecheck(symbols);
        //body.typcheck(symbols);
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        var leftType = left.getType(symbols);
        var rightType = right.getType(symbols);

        boolean bothNumeric = isNumeric(leftType) && isNumeric(rightType);
        boolean bothBoolean = leftType == PrimitiveType.Boolean && rightType == PrimitiveType.Boolean;
        boolean bothObjects = leftType instanceof ClassType && rightType instanceof ClassType;

        String instrOp = switch(op) {
            case "==" -> "eq";
            case "!=" -> "ne";
            case "<" -> "lt";
            case "<=" -> "le";
            case ">" -> "gt";
            case ">=" -> "ge";
            default -> throw new RuntimeException("Internal compiler error");
        };

        String trueLabel = "trueCase";
        String endLabel = "end";

        if (leftType == PrimitiveType.Int && rightType == PrimitiveType.Int
            || leftType == PrimitiveType.Boolean
            || leftType instanceof ClassType) {
            left.generateCode(out, symbols);
            right.generateCode(out, symbols);
            if (leftType instanceof ClassType)
                out.printf("if_acmp%s\n", instrOp, trueLabel);
            else
                out.printf("if_icmp%s\n", instrOp, trueLabel);

            // false case
            out.println("iconst_0");
            out.printf("goto %s\n", endLabel);

            // true case
            out.printf("%s:\n", trueLabel);
            out.println("iconst_1");
            out.printf("%s:\n", endLabel);

        }

        else if (leftType == PrimitiveType.Double || rightType == PrimitiveType.Double) {
            left.generateCode(out, symbols);
            if (leftType == PrimitiveType.Int)
                    out.println("i2d");

            right.generateCode(out, symbols);
            if (rightType == PrimitiveType.Int)
                out.println("i2d");

            out.println("dcmpg"); // compares the two doubles on the stack
            // now the top of the stack is either
            // - -1 if left < right
            // - 0 if left = right
            // - 1 if left > right

            // Say we're evaluating a condition like x < y
            // We now have a -1, 0, or 1 on the stack; we should branch to true if it's -1
            //      -> iflt




            out.printf("if_icmp%s\n", instrOp, trueLabel);

            // false case
            out.println("iconst_0");
            out.printf("goto %s\n", endLabel);

            // true case
            out.printf("%s:\n", trueLabel);
            out.println("iconst_1");
            out.printf("%s:\n", endLabel);

        }

        // ==:  0 0 -> true
        //      0 1 -> false
        //      1 0 -> false
        //      1 1 -> true

        else if (leftType == PrimitiveType.Int && rightType == PrimitiveType.Int) {
            left.generateCode(out, symbols);
            right.generateCode(out, symbols);
            out.printf("if_icmp%s\n", instrOp, trueLabel);

            // false case
            out.println("iconst_0");
            out.printf("goto %s\n", endLabel);

            // true case
            out.printf("%s:\n", trueLabel);
            out.println("iconst_1");
            out.printf("%s:\n", endLabel);

        }
    }

    private boolean isNumeric(Type type) {
        return type == PrimitiveType.Int || type == PrimitiveType.Double;
    }
}