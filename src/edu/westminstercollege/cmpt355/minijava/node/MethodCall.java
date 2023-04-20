package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public record MethodCall(ParserRuleContext ctx, Expression callee, String methodName, List<Expression> arguments) implements Expression {

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        callee.typecheck(symbols);
        for (var arg : arguments)
            arg.typecheck(symbols);
        findMethod(symbols);
    }

    @Override
    public Type getType(SymbolTable symbols) {
        try {
            return findMethod(symbols).returnType();
        } catch (SyntaxException ex) {
            throw new RuntimeException("Internal compiler error", ex);
        }
    }

    @Override
    public List<? extends Node> children() {
        var children = new ArrayList<Expression>();
        children.add(callee);
        children.addAll(arguments);
        return children;
    }

    @Override
    public String getNodeDescription() {
        return String.format("MethodCall [methodName=%s]", methodName);
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        var calleeType = callee.getType(symbols);
        callee.generateCode(out, symbols);

        for (var arg : arguments)
            arg.generateCode(out, symbols);

        try {
            var opcode = (calleeType instanceof StaticType) ? "invokestatic" : "invokevirtual";
            out.printf("%s %s\n", opcode, getMethodSpec(symbols));
        } catch (SyntaxException ex) {
            throw new RuntimeException("Internal compiler error", ex);
        }
    }

    private Method findMethod(SymbolTable symbols) throws SyntaxException {
        var exprType = callee.getType(symbols);
        if (!(exprType instanceof ClassType classType))
            throw new SyntaxException(this, String.format("Call to method %s of non-class type %s", methodName, exprType));

        var argumentTypes = arguments.stream()
                .map(arg -> arg.getType(symbols))
                .toList();

        var maybeMethod = symbols.findMethod(classType, methodName, argumentTypes);
        if (maybeMethod.isEmpty())
            throw new SyntaxException(this, String.format("Call to nonexistent method %s(%s) of class %s", methodName, argumentTypes, classType.getClassName()));

        return maybeMethod.get();
    }

    public String getMethodSpec(SymbolTable symbols) throws SyntaxException {
        var method = findMethod(symbols);
        String className = method.containingType().getClassName();

        var jClass = symbols.findJavaClass(className).get();
        var sb = new StringBuilder();
        sb.append(String.format("%s/%s", jClass.getName().replace('.', '/'), methodName));
        sb.append('(');

        for (var param : method.parameterTypes()) {
            sb.append(switch (param) {
                case PrimitiveType pt -> switch (pt) {
                    case Int -> "I";
                    case Double -> "D";
                    case Boolean -> "Z";
                };
                case ClassType ct -> String.format("L%s;", ct.getClassName().replace('.', '/'));
                default -> throw new RuntimeException(String.format("Internal compiler error: parameter of type %s", param));
            });
        }
        sb.append(')');

        sb.append(switch (method.returnType()) {
            case PrimitiveType pt -> switch (pt) {
                case Int -> "I";
                case Double -> "D";
                case Boolean -> "Z";
            };
            case ClassType ct -> String.format("L%s;", ct.getClassName().replace('.', '/'));
            case VoidType vt -> "V";
            default -> throw new RuntimeException(String.format("Internal compiler error: return type of type %s", method.returnType()));
        });

        return sb.toString();
    }
}
