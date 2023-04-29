package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.SyntaxException;
import org.antlr.v4.runtime.ParserRuleContext;
import edu.westminstercollege.cmpt355.minijava.PrimitiveType;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
    classNodes
returns [ClassNode n]
    : (methods+=method)* EOF {
        var methos = new ArrayList<MethodBody>();
        for (var method : $methods)
            methos.add(method.n);
        $n = new ClassNode($ctx, methos);
    }
    ;

method
    : NAME '(' (args+=expr (',' args+=expr )*)? ')' block {
        var arguments = new ArrayList<Expression>();
        for (var arg : $args)
            arguments.add(arg.n);
        $n = new MethodDefinition($ctx, $NAME.text, arguments, $block.n);
    }
    ;
 */

public record ClassNode(ParserRuleContext ctx, List<Import> imports, List<FieldDefinition> fieldDefinitions, List<MethodDefinition> methods, Optional<MainMethodDefinition> main) implements Node {
    @Override
    public List<? extends Node> children() {
        List<Node> list = new ArrayList<>(imports);
        list.addAll(fieldDefinitions);
        list.addAll(methods);

        if (main.isPresent())
            list.add(main.orElseThrow());

        return list;
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        for (var method : methods) {
            method.typecheck(symbols);
        }
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        out.printf(".class public %s\n", symbols.getCompilingClassName());
        out.printf(".super java/lang/Object\n");
        out.println();

        for (var field : fieldDefinitions) {
            String class_name = symbols.classFromType(field.type().type()).orElseThrow().getName().replace('.', '/');

            if (field.type().type() == PrimitiveType.Int)
                out.printf(".field public %s I\n", field.name());
            else if (field.type().type() == PrimitiveType.Double)
                out.printf(".field public %s D\n", field.name());
            else if (field.type().type() == PrimitiveType.Boolean)
                out.printf(".field public %s Z\n", field.name());
            else
                out.printf(".field public %s L%s;\n", field.name(), class_name);
        }

        out.println("\n.method public <init>()V");
        out.println(".limit stack 100");
        out.println(".limit locals 1");
        out.println("aload_0");
        out.println("invokespecial java/lang/Object/<init>()V");
        for (var field : fieldDefinitions)
            field.generateCode(out, symbols);
        out.println("return");
        out.println(".end method");

        out.println();

        for (var methodDef : methods)
            methodDef.generateCode(out, symbols);

        if (main.isPresent())
            main.orElseThrow().generateCode(out, symbols);

        // public static void main(String args[])
        out.printf(".method public static main([Ljava/lang/String;)V\n");
        out.printf(".limit stack 100\n");
        out.printf(".limit locals %d\n", symbols.getVariableCount() + 1);
        out.printf("new %s\n", symbols.getCompilingClassName());
        out.println("dup");
        out.printf("invokespecial %s/<init>()V\n", symbols.getCompilingClassName());
        if (main.isPresent())
            out.printf("invokevirtual %s/main()V\n", symbols.getCompilingClassName());
        out.println("return");
        out.println(".end method");
    }
}
