package edu.westminstercollege.cmpt355.minijava;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    public static void main(String... args) throws IOException, SyntaxException {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();


        var lexer = new MiniJavaLexer(CharStreams.fromFileName(input));
        var parser = new MiniJavaParser(new CommonTokenStream(lexer));

        AST ast = new AST();

        System.out.println(ast);
    }
}
