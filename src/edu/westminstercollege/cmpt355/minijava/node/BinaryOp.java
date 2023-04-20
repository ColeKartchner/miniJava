package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;

public record BinaryOp(ParserRuleContext ctx, Expression left, Expression right, String op) implements Expression {

    @Override
    public String getNodeDescription() {
        return String.format("BinaryOp [op: %s]", op);
    }

    @Override
    public List<? extends Node> children() {
        return List.of(left, right);
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        // Make sure the children pass their checks
        left.typecheck(symbols);
        right.typecheck(symbols);

        // Now that they have passed, we can use their types
        Type leftType = left.getType(symbols),
                rightType = right.getType(symbols);

        // Insert code that throws a SyntaxException if leftType and rightType
        // don't make sense for this operation (will depend on the value of op!)
        boolean nonNumericOperand = leftType instanceof ClassType || rightType instanceof ClassType
                || leftType == PrimitiveType.Boolean || rightType == PrimitiveType.Boolean
                || leftType instanceof VoidType || rightType instanceof VoidType;
        if ("+".equals(op)) {
            if (leftType.equals(ClassType.String) || rightType.equals(ClassType.String)) {
                if (leftType == VoidType.Instance || rightType == VoidType.Instance)
                    throw new SyntaxException(this, "Cannot concatenate void value");
            } else if (nonNumericOperand)
                throw new SyntaxException(this, String.format("Bad types for operator: %s + %s", leftType, rightType));
        } else {
            if (nonNumericOperand)
                throw new SyntaxException(this, String.format("Bad types for operator: %s %s %s", leftType, op, rightType));
        }
    }

    @Override
    public Type getType(SymbolTable symbols) {
        // getType() should always be called *after* typecheck(), so at this
        // point we know that the children have already passed their checks.
        // We are free to use getType() to get their types.

        Type leftType = left.getType(symbols),
                rightType = right.getType(symbols);

        // Write code that returns the appropriate type based on op, leftType,
        // and rightType
        if ("+".equals(op) && (leftType.equals(ClassType.String) || rightType.equals(ClassType.String)))
            return ClassType.String;
        else if (leftType == PrimitiveType.Double || rightType ==  PrimitiveType.Double)
            return PrimitiveType.Double;
        else
            return PrimitiveType.Int;
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        var leftType = left.getType(symbols);
        var rightType = right.getType(symbols);
        var type = getType(symbols);

        if (type == PrimitiveType.Int) {
            left.generateCode(out, symbols);
            right.generateCode(out, symbols);
            out.println(instructionFor(op, type));
        } else if (type == PrimitiveType.Double) {
            left.generateCode(out, symbols);
            if (leftType == PrimitiveType.Int)
                out.println("i2d");

            right.generateCode(out, symbols);
            if (rightType == PrimitiveType.Int)
                out.println("i2d");

            out.println(instructionFor(op, type));
        } else if (type.equals(ClassType.String)) {
            left.generateCode(out, symbols);
            generateValueToString(out, leftType);

            right.generateCode(out, symbols);
            generateValueToString(out, rightType);

            out.println("invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;");
        } else
            throw new RuntimeException(String.format("Internal compiler error: unimplemented binaryOp for %s %s %s → %s", leftType, op, rightType, type));
    }

    private String instructionFor(String op, Type type) {
        if (!(type instanceof PrimitiveType ptype))
            throw new RuntimeException(String.format("Internal compiler error: binary op instruction for operator %s, type %s", op, type));
        String prefix = switch (ptype) {
            case Int    -> "i";
            case Double -> "d";
            default     -> throw new RuntimeException(String.format("Internal compiler error: no prefix for type %s", type));
        };

        String opName = switch (op) {
            case "+"    -> "add";
            case "-"    -> "sub";
            case "*"    -> "mul";
            case "/"    -> "div";
            case "%"    -> "rem";
            default     -> throw new RuntimeException(String.format("Internal compiler error: no opName for operator %s", op));
        };

        return prefix + opName;
    }

    private void generateValueToString(PrintWriter out, Type type) {
        if (type == PrimitiveType.Int)
            out.println("invokestatic java/lang/String/valueOf(I)Ljava/lang/String;");
        else if (type == PrimitiveType.Double)
            out.println("invokestatic java/lang/String/valueOf(D)Ljava/lang/String;");
        else if (type == PrimitiveType.Boolean)
            out.println("invokestatic java/lang/String/valueOf(Z)Ljava/lang/String;");
        else if (type.equals(ClassType.String))
            ; // do nothing
        else if (type instanceof ClassType)
            out.println("invokevirtual java/lang/String/valueOf(Ljava/lang/Object;)Ljava/lang/String;");
        else
            throw new RuntimeException(String.format("Internal compiler error: unimplemented valueToString for %s", type));
    }
}
