package edu.westminstercollege.cmpt355.minijava;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

public class Main {

    public static void main(String... args) throws IOException {
        final String SOURCE_FILE = "test_programs/test.minijava";
        final String CLASS_NAME = getClassNameFromPath(SOURCE_FILE);

        System.out.printf("Compiling class %s from %s...\n", CLASS_NAME, SOURCE_FILE);

        var lexer = new MiniJavaLexer(CharStreams.fromFileName(SOURCE_FILE));
        var parser = new MiniJavaParser(new CommonTokenStream(lexer));

        var program = parser.goal().n;
        AST.print(program);

        var compiler = new Compiler(program, CLASS_NAME);
        try {
            compiler.compile(Path.of("test_output"));
        } catch (SyntaxException ex) {
            if (ex.getNode() != null)
                System.err.printf("At line %d: %s\n", ex.getNode().ctx().start.getLine(), ex.getMessage());
            else
                System.err.printf("At unknown line: %s\n", ex.getMessage());
            System.exit(1);
        }

        jasmin.Main.main(new String[] {
                "-d", "out/test_compiled",
                String.format("test_output/%s.j", CLASS_NAME)
        });

        try {
            var compiledClass = Class.forName(CLASS_NAME);
            var compiledMainMethod = compiledClass.getMethod("main", String[].class);
            System.out.printf("——— Running compiled class %s ———\n", CLASS_NAME);
            compiledMainMethod.invoke(null, new Object[] { new String[0] });
            System.out.println("——— End of output ———");
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException ex) {
            System.err.println("Unable to execute newly-compiled program: class or method not found!");
        } catch (InvocationTargetException ex) {
            // An exception was thrown by the compiled program (not a compiler problem 🙂)
            ex.getTargetException().printStackTrace();
        }
    }

    private static String getClassNameFromPath(String path) {
        Path p = Path.of(path);
        String filename = p.getFileName().toString();
        int index = filename.indexOf('.');
        return filename.substring(0, index);
    }
}
