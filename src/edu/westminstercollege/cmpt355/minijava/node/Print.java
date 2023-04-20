package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;

public record Print(ParserRuleContext ctx, List<Expression> arguments) implements Expression {

    @Override
    public List<? extends Node> children() {
        return arguments;
    }

    @Override
    public Type getType(SymbolTable symbols) {
        return VoidType.Instance;
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        for (var argument : arguments)
            argument.typecheck(symbols);
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        for (var arg : arguments) {
            var argType = arg.getType(symbols);

            out.println("getstatic java/lang/System/out Ljava/io/PrintStream;");
            arg.generateCode(out, symbols);

            String argDesc;
            if (argType == PrimitiveType.Int)
                argDesc = "I";
            else if (argType == PrimitiveType.Double)
                argDesc = "D";
            else if (argType == PrimitiveType.Boolean)
                argDesc = "Z";
            else if (argType.equals(ClassType.String))
                argDesc = "Ljava/lang/String;";
            else if (argType instanceof ClassType)
                argDesc = "Ljava/lang/Object;";
            else
                throw new RuntimeException(String.format("Internal compiler error: printing argument of type %s", argType));

            out.printf("invokevirtual java/io/PrintStream/print(%s)V\n", argDesc);
        }

        out.println("getstatic java/lang/System/out Ljava/io/PrintStream;");
        out.println("invokevirtual java/io/PrintStream/println()V");
    }
}
