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
    private final Typechecker typechecker = new Typechecker();

    public Compiler(Block block, String className) {
        this.block = block;
        this.className = className;
    }

    public void compile(Path outputDir) throws IOException, SyntaxException {
        Path asmFilePath = outputDir.resolve(className + ".j");
        try (var out = new PrintWriter(Files.newBufferedWriter(asmFilePath))) {
            this.out = out;
            resolveSymbols(block);
            Typechecker typechecker = new Typechecker();
            typechecker.typecheck(symbols, block);

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
            symbols.allocateLocalVariable(1);
            out.printf(".limit locals %d\n", symbols.getVariableCount());
            out.println();

            // Generate code for program here 🙂
            // Generate code for each statement of the program
            // for (var statement : program.statements()) {
            //   generateCode(statement);
            //}

            out.printf("return\n");
            out.printf(".end method\n");
        }
    }

    private void resolveSymbols(Block block) throws SyntaxException {
        AST.postOrder(block, node -> {
            switch (node) {
                // This is if the variable doesn't exist
                case VariableAccess(ParserRuleContext ctx, String name) -> {
                    var variableName = symbols.findVariable(name);
                    if (variableName.isEmpty())
                        throw new SyntaxException(node, String.format("Variable '%s' is undefined. Please declare it before accessing it.", name));
                    //System.out.println(variableName.get().getType().toString() + " " + variableName.get().getName());
                }
                // This is if the variable is trying to be decalred twice
                case DeclarationItem(ParserRuleContext ctx, String name, Optional<Expression> initializer) -> {
                    if(symbols.findVariable(name).isPresent()){
                        throw new SyntaxException(node, String.format("Variable '%s' has already been declared. Please use a different variable name.", name));
                    }
                    else {
                        symbols.registerVariable(name);
                    }
                }
                case Assignment(ParserRuleContext ctx, Expression target, Expression value) -> {
                    if (target instanceof VariableAccess expr) {
                        if (symbols.findVariable(expr.variableName()).isEmpty()) {
                            throw new SyntaxException(node, String.format("Invalid assignment. Variable ('%s') must be declared before being assigned to.", expr.variableName()));
                        }
                    }
                    else {
                        throw new SyntaxException(node, String.format(" Invalid assignment. The left-hand side ('%s') should be a variable", ctx.getChild(0).getText()));
                    }
                }
                default -> {}
            }
        });
    }

    private void generateCode(PrintWriter out, SymbolTable symbols, Node node) throws SyntaxException {
        switch (node) {
            case EmptyStatement(ParserRuleContext ctx) -> {}

            case Block(ParserRuleContext ctx, List<Statement> statements) -> {
                for (var statement : statements) {
                    out.printf("\n.line %d\n", statement.ctx().getStart().getLine());
                    generateCode(out, symbols, statement);
                }
            }
            case ExpressionStatement(ParserRuleContext ctx, Expression expression) -> {
                generateCode(out, symbols, expression);
                Type exprType = typechecker.getType(symbols, expression);
                if (exprType.equals(VoidType.Instance)){}
                else if (exprType.equals(PrimitiveType.Double))
                    out.printf("pop2\n");
                else if (exprType.equals(PrimitiveType.Int) || exprType.equals(PrimitiveType.Boolean))
                    out.printf("pop\n");
            }
            case DoubleLiteral(ParserRuleContext ctx, String text) -> {
                out.printf("ldc2_w %f\n", Double.parseDouble(text));
            }
            case IntLiteral(ParserRuleContext ctx, String text) -> {
                out.printf("ldc %d\n", Integer.parseInt(text));
            }
            case BooleanLiteral(ParserRuleContext ctx, String text) -> {
                if (text.equals("true"))
                    out.printf("iconst_1\n");
                else
                    out.printf("iconst_0\n");
            }
            case StringLiteral(ParserRuleContext ctx1, String text) -> {
                out.printf("ldc %s\n", text);
            }
            case VariableDeclarations(ParserRuleContext ctx, TypeNode type, List<DeclarationItem> items) -> {
                for(var item : items){
                    generateCode(out, symbols, item);
                }
            }
            case DeclarationItem(ParserRuleContext ctx, String name, Optional<Expression> initializer) -> {
                if(initializer.isPresent()) {
                    generateCode(out, symbols, initializer.get());
                    Variable var = symbols.findVariable(name).get();

                    Type varType = var.getType();
                    Type exprType = typechecker.getType(symbols, initializer.get());
                    var stringType = new ClassType("String");
                    if(varType.equals(PrimitiveType.Int) || varType.equals(PrimitiveType.Boolean)) {
                        out.printf("istore %d\n", var.getIndex());
                    }
                    else if(varType.equals(PrimitiveType.Double) && exprType.equals(PrimitiveType.Int)){
                        out.printf("i2d\n");
                        out.printf("dstore %d\n", var.getIndex());
                    }
                    else if(varType.equals(stringType)){
                        out.printf("astore %d\n", var.getIndex());
                    }
                    else {
                        out.printf("dstore %d\n", var.getIndex());
                    }

                }
            }
            case Print(ParserRuleContext ctx, List<Expression> arguments) -> {
                String printlnArg = "";
                var stringType = new ClassType("String");
                Type exprType;
                for (var expression : arguments) {
                    out.printf("getstatic java/lang/System/out Ljava/io/PrintStream;\n");
                    generateCode(out, symbols, expression);
                    exprType = typechecker.getType(symbols, expression);
                    if (exprType.equals(PrimitiveType.Int))
                        printlnArg = "I";
                    else if (exprType.equals(PrimitiveType.Double))
                        printlnArg = "D";
                    else if (exprType.equals(PrimitiveType.Boolean))
                        printlnArg = "Z";
                    else if (exprType.equals(stringType))
                        printlnArg = "Ljava/lang/String;";
                    else
                        throw new SyntaxException("Print argument Unimplemented");
                    out.printf(String.format("invokevirtual java/io/PrintStream/println(%s)V\n", printlnArg));
                }
            }
            case Assignment(ParserRuleContext ctx, Expression target, Expression value) -> {
                Variable var = symbols.findVariable(((VariableAccess)target).variableName()).get();
                Type exprType = typechecker.getType(symbols, target);
                Type assigType = typechecker.getType(symbols, value);

                generateCode(out, symbols, value);
                var stringType = new ClassType("String");
                if (exprType.equals(PrimitiveType.Double) && assigType.equals(PrimitiveType.Int)) {
                    out.printf("i2d\n");
                    out.printf("dup2\n");
                    out.printf("dstore_%d\n", var.getIndex());
                }
                else if (exprType.equals(PrimitiveType.Int) || exprType.equals(PrimitiveType.Boolean)){
                    out.printf("dup\n");
                    out.printf("istore %d\n", var.getIndex());
                }
                else if (exprType.equals(stringType)){
                    out.printf("dup\n");
                    out.printf("astore %d\n", var.getIndex());
                }
                else{
                    out.printf("dup2\n");
                    out.printf("dstore %d\n", var.getIndex());
                }
            }
            case VariableAccess(ParserRuleContext ctx, String variableName) -> {
                Variable var = symbols.findVariable(variableName).get();
                var stringType = new ClassType("String");
                if(var.getType().equals(PrimitiveType.Double)){
                    out.printf("dload %d\n", var.getIndex());
                }
                else if (var.getType().equals(stringType)){
                    out.printf("aload %d\n", var.getIndex());
                }
                else
                    out.printf("iload %d\n", var.getIndex());
            }
            case BinaryOp(ParserRuleContext ctx, Expression left, Expression right, String op) -> {
                int numberOfInts = 0;
                generateCode(out, symbols, left);

                var leftType = typechecker.getType(symbols, left);
                if (leftType.equals(PrimitiveType.Int)) {
                    out.println("i2d");
                    numberOfInts++;
                }
                generateCode(out, symbols, right);
                var rightType = typechecker.getType(symbols, right);
                if (rightType.equals(PrimitiveType.Int)) {
                    out.println("i2d");
                    numberOfInts++;
                }

                switch (op) {
                    case "+" -> out.println("dadd");
                    case "-" -> out.println("dsub");
                    case "*" -> out.println("dmul");
                    case "/" -> out.println("ddiv");
                    case "%" -> out.println("drem");
                }
                if (numberOfInts == 2)
                    out.println("d2i");
            }
            default -> {
                throw new SyntaxException("Unimplemented");
            }
        }
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
