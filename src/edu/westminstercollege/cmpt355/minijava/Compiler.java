package edu.westminstercollege.cmpt355.minijava;

import edu.westminstercollege.cmpt355.minijava.node.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Compiler {

    // Commented out until we have our AST nodes defined...
    private SymbolTable symbols = new SymbolTable();
    private PrintWriter out;
    private final Block program;
    private final String className;

    public Compiler(Block program, String className) {
        this.program = program;
        this.className = className;
    }

    public SymbolTable getSymbols() {
        return symbols;
    }

    public void compile(Path outputDir) throws IOException, SyntaxException {
        Path asmFilePath = outputDir.resolve(className + ".j");
        try (var out = new PrintWriter(Files.newBufferedWriter(asmFilePath))) {
            this.out = out;
            symbols.allocateLocalVariable(1);
            resolveSymbols(program);
            program.typecheck(symbols);

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
            //out.printf(".limit locals 1\n");
            out.printf(".limit locals %d\n", symbols.getVariableCount());
            out.println();

            program.generateCode(out, symbols);
            // Generate code for program here 🙂
            // Generate code for each statement of the program
//            for (var statement : program.statements())
//                generateCode(statement);

            //program.statements().forEach(this::generateCode);

            out.printf("return\n");
            out.printf(".end method\n");
        }
    }

     // Make sure that all symbols (in this case, names of variables) make sense,
     // i.e. we should not be using the value of a variable before we have assigned
     // to it (Eval does not have declarations).
     private void resolveSymbols(Block program) throws SyntaxException {
        AST.postOrder(program, node -> {
            switch (node) {
                /*case VariableDeclarations(ParserRuleContext ctx, TypeNode typeNode, List<DeclarationItem> items) -> {
                    for (var item : items) {
                        if (symbols.findVariable(item.className()).isPresent())
                            throw new SyntaxException(node, String.format("Multiple declaration of variable %s!", item.className()));
                        else
                            symbols.registerVariable(item.className(), typeNode.type());
                    }
                }*/

                case DeclarationItem(ParserRuleContext ctx, String name, Optional<Expression> initializer) -> {
                    if (symbols.findVariable(name).isPresent())
                        throw new SyntaxException(node, String.format("Multiple declaration of variable %s!", name));
                    else
                        symbols.registerVariable(name, ((MiniJavaParser.DeclarationContext)ctx.parent).n.type().type());
                }

                case VariableAccess(ParserRuleContext ctx, String name) -> {
                    if (symbols.findVariable(name).isEmpty() && symbols.findJavaClass(name).isEmpty())
                        throw new SyntaxException(node, String.format("Use of undeclared variable %s!", name));
                    // For debugging purposes:
                    //System.out.printf("Access to variable %s\n", symbols.findVariable(className).get());
                }

                case Assignment(ParserRuleContext ctx, LValue lhs, Expression rhs) -> {
                    if (!(lhs instanceof VariableAccess va))
                        throw new SyntaxException(node, "Attempt to assign to something that is not a variable!");
                    else if (symbols.findVariable(va.variableName()).isEmpty())
                        throw new SyntaxException(node, String.format("Use of undeclared variable %s!", va.variableName()));
                }
                    /*
                case Assignment(String className, Expression e) -> symbols.registerVariable(className);
                case VariableAccess(String className) -> {
                    if (symbols.findVariable(className).isEmpty())
                        // No variable found!
                        throw new SyntaxException(String.format("Variable used before assignment: %s", className));
                }*/
                default -> {}
            }
        });
    }

    /*
    private void generateCode(Statement statement) {
        switch (statement) {
            case Print(List<PrintArgument> args) -> {
                // Print each argument individually (using generateCode(PrintArgument))
                // then do a println.
                for (var arg : args)
                    generateCode(arg);
                //args.forEach(this::generateCode);
                out.println("getstatic java/lang/System/out Ljava/io/PrintStream;");
                out.println("invokevirtual java/io/PrintStream/println()V");
            }
            default ->
                    throw new RuntimeException(String.format("Unimplemented: %s", statement.getNodeDescription()));
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
                // Call the Math.sqrt() method
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

            // Variable access
            // Input
            default ->
                    throw new RuntimeException(String.format("Unimplemented: %s", expr.getNodeDescription()));
        }
    }

    // */
}
