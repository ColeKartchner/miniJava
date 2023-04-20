package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;

public record ConstructorCall(ParserRuleContext ctx, String className, List<Expression> arguments) implements Expression {

    @Override
    public Type getType(SymbolTable symbols) {
        return new ClassType(symbols.findJavaClass(className).get().getName());
    }

    @Override
    public List<? extends Node> children() {
        return arguments;
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        for (Expression argument : arguments)
            argument.typecheck(symbols);
        findConstructor(symbols);
    }

    @Override
    public String getNodeDescription() {
        return String.format("ConstructorCall [className=%s]", className);
    }

    private Method findConstructor(SymbolTable symbols) throws SyntaxException {
        // Get the class from the symbol table
        var argTypes = arguments().stream().map(e -> e.getType(symbols)).toList();
        var maybeCtor = symbols.findConstructor(new ClassType(className), argTypes);
        if (maybeCtor.isEmpty())
            throw new SyntaxException(this, String.format("No constructor for class %s found with arguments %s" , className, argTypes));
        return maybeCtor.get();
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        try {
            var ctor = findConstructor(symbols);

            out.printf("new %s\n", ((ClassType)ctor.containingType()).getClassName().replace('.', '/'));
            out.printf("dup\n");

            for (Expression argument : arguments)
                argument.generateCode(out, symbols);

            out.printf("invokenonvirtual %s\n", getMethodSpec(symbols));
        } catch (SyntaxException e) {
            throw new RuntimeException("Internal compiler error", e);
        }
    }

    public String getMethodSpec(SymbolTable symbols) throws SyntaxException {
        var ctor = findConstructor(symbols);
        String className = ctor.containingType().getClassName();

        var sb = new StringBuilder();
        sb.append(String.format("%s/%s", className.replace('.', '/'), "<init>"));
        sb.append('(');

        for (var param : ctor.parameterTypes()) {
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
        sb.append(")V");

        return sb.toString();
    }
}
