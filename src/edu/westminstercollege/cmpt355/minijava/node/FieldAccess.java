package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;

public record FieldAccess(ParserRuleContext ctx, Expression expr, String fieldName) implements Expression {

    @Override
    public String getNodeDescription() {
        return String.format("FieldAccess [fieldName=%s]", fieldName);
    }

    @Override
    public List<? extends Node> children() {
        return List.of(expr);
    }

    private Field findField(SymbolTable symbols) throws SyntaxException {
        var exprType = expr.getType(symbols);
        if (!(exprType instanceof ClassType cType))
            throw new SyntaxException(this, String.format("Access to field %s of non-class type %s", fieldName, exprType));

        var maybeField = symbols.findField(cType, fieldName);
        if (maybeField.isEmpty())
            throw new SyntaxException(this, String.format("Access to nonexistent field %s of class %s", fieldName, cType.getClassName()));

        return maybeField.get();
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        expr.typecheck(symbols);
        findField(symbols);
    }

    @Override
    public Type getType(SymbolTable symbols) {
        try {
            return findField(symbols).type();
        } catch (SyntaxException ex) {
            throw new RuntimeException("Internal compiler error", ex);
        }
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        var exprType = expr.getType(symbols);
        expr.generateCode(out, symbols);
        try {
            var field = findField(symbols);

            boolean staticField = field.containingType() instanceof StaticType;

            var opcode = staticField ? "getstatic" : "getfield";

            String fieldDesc = switch (field.type()) {
                case PrimitiveType p -> switch (p) {
                    case Int -> "I";
                    case Double -> "D";
                    case Boolean -> "Z";
                };
                case ClassType ct -> Reflect.classForName(ct.getClassName()).get().descriptorString();

                default -> throw new RuntimeException(String.format("Internal compiler error: invalid field type %s", field.type()));
            };

            out.printf("%s %s %s\n", opcode, getFieldSpec(symbols), fieldDesc);
        } catch (SyntaxException ex) {
            throw new RuntimeException("Internal compiler error", ex);
        }
    }

    public String getFieldSpec(SymbolTable symbols) throws SyntaxException {
        var field = findField(symbols);
        String className = field.containingType().getClassName();

        var jClass = symbols.findJavaClass(className).get();
        return String.format("%s/%s", jClass.getName().replace('.', '/'), fieldName);
    }

}
