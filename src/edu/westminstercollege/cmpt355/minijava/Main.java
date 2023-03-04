package edu.westminstercollege.cmpt355.minijava;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.nio.file.Path;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    public static void main(String... args) throws IOException, SyntaxException {
        final String MINIJAVA_FILE = "Input/hello.miniJava";
        final String CLASS_NAME = getClassNameFromPath(MINIJAVA_FILE);

        System.out.printf("Compiling class %s from %s...\n", CLASS_NAME, MINIJAVA_FILE);

        var lexer = new MiniJavaLexer(CharStreams.fromFileName(MINIJAVA_FILE));
        var parser = new MiniJavaParser(new CommonTokenStream(lexer));

        var goal = parser.goal().n;
        AST.print(goal);
    }

    private static String getClassNameFromPath(String path) {
        Path p = Path.of(path);
        String filename = p.getFileName().toString();
        int index = filename.indexOf('.');
        return filename.substring(0, index);
    }
}
