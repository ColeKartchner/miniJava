package edu.westminstercollege.cmpt355.minijava;

import edu.westminstercollege.cmpt355.minijava.node.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;


public class Compiler {

    // Commented out until we have our AST nodes defined..
    private SymbolTable symbols = new SymbolTable();
    private PrintWriter out;
    private final Block block;
    private final String className;

    public Compiler(Block block, String className) {
        this.block = block;
        this.className = className;
    }

    public void compile(Path outputDir) throws IOException, SyntaxException {
        Path asmFilePath = outputDir.resolve(className + ".j");
        try (var out = new PrintWriter(Files.newBufferedWriter(asmFilePath))) {
            this.out = out;
            resolveSymbols(block);

            out.printf(".class public %s\n", className);
            out.printf(".super java/lang/Object\n");
            out.println();
            out.println(".field private static in Ljava/util/Scanner;");
            out.println();
            out.printf("""
                    .method static <clinit>()V
                    .limit stack 3
                    .limit locals 0
                    new java/util/Scanner
                    dup
                    getstatic java/lang/System/in Ljava/io/InputStream;
                    invokenonvirtual java/util/Scanner/<init>(Ljava/io/InputStream;)V
                    putstatic %s/in Ljava/util/Scanner;
                    return
                    .end method
                    
                    """, className);
            out.printf(".method public static main([Ljava/lang/String;)V\n");
            out.printf(".limit stack 100\n");
            out.printf(".limit locals 1\n");
            out.printf(".limit locals %d\n", symbols.getVariableCount() * 2 + 1); // + 1 because of args
            out.println();

            // Generate code for program here 🙂
            // Generate code for each statement of the program
            //for (var statement : program.statements()) {
            //    generateCode(statement);
            //}

            out.printf("return\n");
            out.printf(".end method\n");
        }
    }

    private void resolveSymbols(Block block) throws SyntaxException {
        AST.preOrder(block, node -> {
            switch (node) {
                // This is if the variable already exists
                case VariableAccess(ParserRuleContext ctx, String name) -> {
                    if (symbols.findVariable(name).isEmpty())
                        throw new SyntaxException(node, String.format("'%s' hasn't been declared yet", name));
                }
                // This is if the variable is trying to be decalred twice
                case DeclarationItem(ParserRuleContext ctx, String name, Optional<Expression> initializer) -> {
                    if(symbols.findVariable(name).isPresent()){
                        throw new SyntaxException(node, String.format("'%s' is already declared", name));
                    }
                    else {
                        symbols.registerVariable(name);
                    }
                }
                case Assignment(ParserRuleContext ctx, Expression target, Expression value) -> {
                    if (symbols.findVariable(target)) {

                    }
                }
                default -> {}
            }
        });
    }


    /*
    private void generateCode(Statement statement) {
        switch (statement) {
            case Print(List<PrintArgument> args) -> {
                // Print each argument individually using generateCode(PrintArgument))
                // Then do a println
                for (var arg : args) {
                    generateCode(arg);
                }
                out.println("getstatic java/lang/System/out Ljava/io/PrintStream;");
                out.println("invokevirtual java/io/PrintStream/println()V");
            }
            case Assignment(String varName, Expression value) -> {
                Variable var = symbols.findVariable(varName).get();
                generateCode(value);
                out.printf("dstore %d\n", var.getIndex());
            }
        }
    }

    private void generateCode(PrintArgument argument) {
        switch (argument) {
            case Expression e -> {
                out.println("getstatic java/lang/System/out Ljava/io/PrintStream;");
                generateCode(e);
                out.println("invokevirtual java/io/PrintStream/print(D)V");
            }
            case StringArgument(String text) -> {
                out.println("getstatic java/lang/System/out Ljava/io/PrintStream;");
                out.printf("ldc %s\n", text);
                out.println("invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V");
            }
        }
    }

    private void generateCode(Expression expr) {
        switch (expr) {
            case Literal(String text) -> {
                out.printf("ldc2_w %f\n", Double.parseDouble(text));
            }
            case Add(Expression left, Expression right) -> {
                generateCode(left);
                generateCode(right);
                out.println("dadd");
            }

            case Subtract(Expression left, Expression right) -> {
                generateCode(left);
                generateCode(right);
                out.println("dsub");
            }

            case Multiply(Expression left, Expression right) -> {
                generateCode(left);
                generateCode(right);
                out.println("dmul");
            }

            case Divide(Expression left, Expression right) -> {
                generateCode(left);
                generateCode(right);
                out.println("ddiv");
            }

            case Negate(Expression child) -> {
                generateCode(child);
                out.println("dneg");
            }

            case SquareRoot(Expression child) -> {
                generateCode(child);
                out.println("invokestatic java/lang/Math/sqrt(D)D");
            }

            case Power(Expression left, Expression right) -> {
                generateCode(left);
                generateCode(right);
                out.println("invokestatic java/lang/Math/pow(DD)D");
            }

            case VariableAccess(String variableName) -> {
                Variable v = symbols.findVariable(variableName).get();
                out.printf("dload %d\n", v.getIndex());
            }

            case Input(List<PrintArgument> args) -> {
                for (var arg : args) {
                    generateCode(arg);

                    out.printf("getstatic %s/in Ljava/util/Scanner;\n", className);
                    out.println("invokevirtual java/util/Scanner/nextDouble()D");
                }
            }

            default ->
                    throw new RuntimeException(String.format("Unimplemented: %s", expr.getNodeDescription()));
        }
    }

    // */
}
