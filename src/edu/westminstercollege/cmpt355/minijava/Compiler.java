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
            generateCode(out, symbols, block);

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
                Type exprType = typechecker.getType(symbols, expression);
                generateCode(out, symbols, expression);
                if (exprType.equals(VoidType.Instance)){}
                else if (exprType.equals(PrimitiveType.Int) || exprType.equals(PrimitiveType.Boolean)) {
                    out.printf("pop\n");
                }
                else if (exprType.equals(PrimitiveType.Double)) {
                    out.printf("pop2\n");
                }
            }
            case IntLiteral(ParserRuleContext ctx, String text) -> {
                out.printf("ldc %d\n", Integer.parseInt(text));
            }
            case DoubleLiteral(ParserRuleContext ctx, String text) -> {
                out.printf("ldc2_w %f\n", Double.parseDouble(text));
            }
            case StringLiteral(ParserRuleContext ctx1, String text) -> {
                out.printf("ldc %s\n", text);
            }
            case BooleanLiteral(ParserRuleContext ctx, String text) -> {
                if (text.equals("true"))
                    out.printf("iconst_1\n");
                else
                    out.printf("iconst_0\n");
            }
            case Print(ParserRuleContext ctx, List<Expression> arguments) -> {
                var stringType = new ClassType("String");
                Type argumentType;
                String printlnArgType = "";

                for (var argument : arguments) {
                    out.printf("getstatic java/lang/System/out Ljava/io/PrintStream;\n");
                    generateCode(out, symbols, argument);
                    argumentType = typechecker.getType(symbols, argument);

                    if (argumentType.equals(PrimitiveType.Int))
                        printlnArgType = "I";
                    else if (argumentType.equals(PrimitiveType.Double))
                        printlnArgType = "D";
                    else if (argumentType.equals(stringType))
                        printlnArgType = "Ljava/lang/String;";
                    else if (argumentType.equals(PrimitiveType.Boolean))
                        printlnArgType = "Z";
                    else
                        throw new SyntaxException("Invalid argument type for print statement");

                    out.printf(String.format("invokevirtual java/io/PrintStream/println(%s)V\n", printlnArgType));
                }
            }
            case VariableDeclarations(ParserRuleContext ctx, TypeNode type, List<DeclarationItem> items) -> {
                for(var item : items){
                    generateCode(out, symbols, item);
                }
            }
            case DeclarationItem(ParserRuleContext ctx, String name, Optional<Expression> initializer) -> {
                Variable variable = symbols.findVariable(name).get();
                Type variableType = variable.getType();
                var stringType = new ClassType("String");

                if(initializer.isPresent()) {
                    Type expressionType = typechecker.getType(symbols, initializer.get());

                    if(variableType.equals(PrimitiveType.Int) || variableType.equals(PrimitiveType.Boolean)) {
                        out.printf("istore %d\n", variable.getIndex());
                    }
                    else if(variableType.equals(PrimitiveType.Double) && expressionType.equals(PrimitiveType.Int)){
                        out.printf("i2d\n");
                        out.printf("dstore %d\n", variable.getIndex());
                    }
                    else if(variableType.equals(stringType)){
                        out.printf("astore %d\n", variable.getIndex());
                    }
                    else {
                        out.printf("dstore %d\n", variable.getIndex());
                    }
                    generateCode(out, symbols, initializer.get());
                }
            }
            case Assignment(ParserRuleContext ctx, Expression targetExpr, Expression valueExpr) -> {
                Variable targetVar = symbols.findVariable(((VariableAccess)targetExpr).variableName()).get();
                Type targetType = typechecker.getType(symbols, targetExpr);
                Type valueType = typechecker.getType(symbols, valueExpr);

                generateCode(out, symbols, valueExpr);
                var stringType = new ClassType("String");

                if (targetType.equals(PrimitiveType.Double) && valueType.equals(PrimitiveType.Int)) {
                    out.printf("i2d\n");
                    out.printf("dup2\n");
                    out.printf("dstore_%d\n", targetVar.getIndex());
                }
                else if (targetType.equals(PrimitiveType.Int) || targetType.equals(PrimitiveType.Boolean)){
                    out.printf("dup\n");
                    out.printf("istore %d\n", targetVar.getIndex());
                }
                else if (targetType.equals(stringType)){
                    out.printf("dup\n");
                    out.printf("astore %d\n", targetVar.getIndex());
                }
                else{
                    out.printf("dup2\n");
                    out.printf("dstore %d\n", targetVar.getIndex());
                }
            }
            case VariableAccess(ParserRuleContext ctx, String name) -> {
                Variable var = symbols.findVariable(name).get();
                Type varType = var.getType();
                var stringType = new ClassType("String");
                if (varType.equals(stringType)){
                    out.printf("aload %d\n", var.getIndex());
                }
                else if(varType.equals(PrimitiveType.Double)){
                    out.printf("dload %d\n", var.getIndex());
                }
                else if (var.getType().equals(PrimitiveType.Int) || var.getType().equals(PrimitiveType.Boolean)) {
                    out.printf("iload %d\n", var.getIndex());
                }
                else {
                    out.printf("aload %d\n", var.getIndex());
                }
            }
            case BinaryOp(ParserRuleContext ctx, Expression left, Expression right, String op) -> {
                int intCount = 0;
                generateCode(out, symbols, left);

                Type leftType = typechecker.getType(symbols, left);
                if (leftType.equals(PrimitiveType.Int)) {
                    out.println("i2d");
                    intCount++;
                }

                generateCode(out, symbols, right);

                Type rightType = typechecker.getType(symbols, right);
                if (rightType.equals(PrimitiveType.Int)) {
                    out.println("i2d");
                    intCount++;
                }

                switch (op) {
                    case "+" -> out.println("dadd");
                    case "-" -> out.println("dsub");
                    case "*" -> out.println("dmul");
                    case "/" -> out.println("ddiv");
                    case "%" -> out.println("drem");
                }

                if (intCount == 2) {
                    out.println("d2i");
                }
            }
            case Cast(ParserRuleContext ctx, TypeNode targetType, Expression expression) -> {
                Type expressionType = typechecker.getType(symbols, expression);
                generateCode(out, symbols, expression);

                Type stringType = new ClassType("String");
                Type intType = new ClassType("Int");
                Type doubleType = new ClassType("Double");
                Type booleanType = new ClassType("Boolean");

                if (expressionType.equals(intType) && targetType.equals(doubleType)) {
                    out.println("i2d");
                }
                else if (expressionType.equals(intType) && targetType.equals(stringType)) {
                    out.println("invokestatic java/lang/String/valueOf()");
                }
                else if (expressionType.equals(doubleType) && targetType.equals(intType)) {
                    out.println("d2i");
                }
                else if (expressionType.equals(doubleType) && targetType.equals(stringType)) {
                    out.println("invokestatic java/lang/String/valueOf()");
                }
                else if (expressionType.equals(booleanType) && targetType.equals(stringType)) {
                    out.println("invokestatic java/lang/String/valueOf()");
                }
                else if (expressionType.equals(stringType) && targetType.equals(stringType)) {
                    out.println("invokestatic java/lang/String/valueOf()");
                }
            }

            case Negate(ParserRuleContext ctx, Expression expression) -> {
                var type = typechecker.getType(symbols, expression);
                if (type == PrimitiveType.Int) {
                    out.println("ineg");
                }
                else if (type == PrimitiveType.Double) {
                    out.println("dneg");
                }
                else {
                    throw new RuntimeException(String.format("Invalid type for negation: %s", type));
                }
            }

            case PreIncrement(ParserRuleContext ctx, Expression target, String op) -> {
                var variableAccess = (VariableAccess)target;
                var variableSymbol = symbols.findVariable(variableAccess.variableName()).get();
                var variableIndex = variableSymbol.getIndex();
                var variableType = typechecker.getType(symbols, variableAccess);

                if (variableType == PrimitiveType.Int) {
                    if (op.equals("++"))
                        out.println(String.format("iinc %d 1", variableIndex));
                    else
                        out.println(String.format("iinc %d -1", variableIndex));
                    out.println(String.format("iload %d", variableIndex));
                }
                else if (variableType == PrimitiveType.Double) {
                    out.println(String.format("dload %d", variableIndex));
                    out.println("dconst_1");
                    if (op.equals("++"))
                        out.println("dadd");
                    else
                        out.println("dsub");
                    out.println("dup2");
                    out.println(String.format("dstore %d", variableIndex));
                }
                else
                    throw new RuntimeException(String.format("Pre-increment cannot be %s", variableType));
            }
            case PostIncrement(ParserRuleContext ctx, Expression target, String op) -> {
                var variableAccess = (VariableAccess) target;
                var variableSymbol = symbols.findVariable(variableAccess.variableName()).get();
                var variableIndex = variableSymbol.getIndex();
                var variableType = typechecker.getType(symbols, variableAccess);

                if (variableType == PrimitiveType.Int) {
                    out.println(String.format("iload %d", variableIndex));
                    if (op.equals("++")) {
                        out.println(String.format("iinc %d 1", variableIndex));
                    } else {
                        out.println(String.format("iinc %d -1", variableIndex));
                    }
                }
                else if (variableType == PrimitiveType.Double) {
                    out.println(String.format("dload %d", variableIndex));
                    out.println("dup2");
                    out.println("dconst_1");
                    if (op.equals("++")) {
                        out.println("dadd");
                    } else {
                        out.println("dsub");
                    }
                    out.println(String.format("dstore %d", variableIndex));
                }
                else {
                    throw new RuntimeException(String.format("Post-increment cannot be %s", variableType));
                }
            }
            case FieldAccess(ParserRuleContext ctx, Expression expr, String name) -> {
                var fieldType = new ClassType("String");
                var expressionType = typechecker.getType(symbols, expr);
                String classDescriptor = symbols.findJavaClass(((ClassType) expressionType).getClassName()).get().descriptorString();
                classDescriptor = classDescriptor.substring(1, classDescriptor.length()-1);
                var fieldSymbol = symbols.findField((ClassType) expressionType, name);
                String fieldDescriptor = "";
                if(fieldSymbol.get().type().equals(PrimitiveType.Double))
                    fieldDescriptor = " D";
                else if (fieldSymbol.get().type().equals(PrimitiveType.Int))
                    fieldDescriptor = " I";
                else if (fieldSymbol.get().type().equals(PrimitiveType.Boolean))
                    fieldDescriptor = " Z";
                else if (fieldSymbol.get().type().equals(VoidType.Instance))
                    fieldDescriptor = " V";
                else if (fieldSymbol.get().type().equals(fieldType))
                    fieldDescriptor = " Ljava/lang/String;";
                else {
                    String fieldTypeString = fieldSymbol.get().type().toString();
                    String fieldTypeDescriptor = " L" + fieldTypeString.substring(10, fieldTypeString.length()-1) + ";";
                    fieldDescriptor = fieldTypeDescriptor.replace('.','/');
                }
                if(expressionType instanceof StaticType) {
                    out.println("Get static field " + classDescriptor + "." + name + fieldDescriptor);
                }
                else {
                    generateCode(out, symbols, expr);
                    out.println("Get instance field " + classDescriptor + "/" + name + fieldDescriptor);
                }
            }

            case MethodCall(ParserRuleContext ctx, Expression expr, String name, List<Expression> args) -> {
                var type = typechecker.getType(symbols, expr);
                String classDesc = symbols.findJavaClass(((ClassType) type).getClassName()).get().descriptorString();
                classDesc = classDesc.substring(1, classDesc.length() - 1);
                List<Type> argTypes = new ArrayList<>();
                for (var arg : args) {
                    var argType = Reflect.typeFromClass(symbols.classFromType(typechecker.getType(symbols, arg)).get()).get();
                    argTypes.add(argType);
                }
                var method = symbols.findMethod((ClassType) type, name, argTypes);
                var retType = method.get().returnType();
                var retTypeStr = symbols.classFromType(retType).get().toString();
                switch (retTypeStr) {
                    case "double" -> retTypeStr = "D";
                    case "boolean" -> retTypeStr = "Z";
                    case "int" -> retTypeStr = "I";
                    case "void" -> retTypeStr = "V";
                    default -> {
                        retTypeStr = retTypeStr.substring(6);
                        retTypeStr = retTypeStr.replace('.', '/');
                        retTypeStr = retTypeStr + ";";
                    }
                }
                if (type instanceof StaticType) {
                    for (var arg : args) {
                        generateCode(out, symbols, arg);
                    }
                    out.print("Get static field " + classDesc + "/" + name);
                    for (var argType : argTypes) {
                        var argTypeStr = symbols.classFromType(argType).get().toString();
                        switch (argTypeStr) {
                            case "double" -> out.print("D");
                            case "boolean" -> out.print("Z");
                            case "int" -> out.print("I");
                            case "void" -> out.print("V");
                            default -> {
                                argTypeStr = argTypeStr.substring(6);
                                argTypeStr = argTypeStr.replace('.', '/');
                                argTypeStr = "L" + argTypeStr + ";";
                                out.print(argTypeStr);
                            }
                        }
                    }
                    out.println(retTypeStr);
                } else {
                    generateCode(out, symbols, expr);
                    for (var arg : args) {
                        generateCode(out, symbols, arg);
                    }
                    out.print("Get non-static field " + classDesc + "/" + name);
                    for (var argType : argTypes) {
                        var argTypeStr = symbols.classFromType(argType).get().toString();
                        switch (argTypeStr) {
                            case "double" -> out.print("D");
                            case "boolean" -> out.print("Z");
                            case "int" -> out.print("I");
                            case "void" -> out.print("V");
                            default -> {
                                argTypeStr = argTypeStr.substring(6);
                                argTypeStr = argTypeStr.replace('.', '/');
                                argTypeStr = "L" + argTypeStr + ";";
                                out.print(argTypeStr);
                            }
                        }
                    }
                    out.println(retTypeStr);
                }
            }
            case ConstructorCall(ParserRuleContext ctx, String name, List<Expression> args) -> {
                var classDescriptor = symbols.findJavaClass(name).get().descriptorString();
                classDescriptor = classDescriptor.substring(1, classDescriptor.length() - 1);
                List<String> parameterTypeDescriptors = new ArrayList<>();
                for (var arg : args) {
                    generateCode(out, symbols, arg);
                    var parameterType = symbols.classFromType(typechecker.getType(symbols, arg)).get().toString();
                    switch (parameterType) {
                        case "int" -> parameterType = "I";
                        case "double" -> parameterType = "D";
                        case "boolean" -> parameterType = "Z";
                        case "void" -> parameterType = "V";
                        default -> {
                            parameterType = parameterType.substring(6);
                            parameterType = parameterType.replace('.', '/');
                            parameterType = parameterType + ";";
                        }
                    }
                    parameterTypeDescriptors.add(parameterType);
                }
                out.println("new " + classDescriptor);
                out.println("dup");
                out.print("Get non-static field " + classDescriptor);
                for (String parameterTypeDescriptor : parameterTypeDescriptors) {
                    out.print(parameterTypeDescriptor);
                }
            }


            default -> {
                throw new SyntaxException("Unimplemented");
            }
        }
    }
}
