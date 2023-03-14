package edu.westminstercollege.cmpt355.minijava;

import edu.westminstercollege.cmpt355.minijava.node.Block;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class Main {

    public static void main(String... args) throws IOException, SyntaxException {
        final String EVAL_FILE = "test_programs/pythag.eval";
        final String CLASS_NAME = getClassNameFromPath(EVAL_FILE);

        System.out.printf("Compiling class %s from %s...\n", CLASS_NAME, EVAL_FILE);

        var lexer = new MiniJavaLexer(CharStreams.fromFileName("test_input/project2.minijava"));
        var parser = new MiniJavaParser(new CommonTokenStream(lexer));

        var goal = parser.goal().n;
        AST.print(goal);

        AST.checkForNulls(goal); // for debugging — throws an exception if AST contains any null nodes



        try {
            var compiler = new Compiler(goal, CLASS_NAME);
            compiler.compile(Path.of("test_output"));

            jasmin.Main.main(new String[]{
                    "-d", "out/test_compiled",
                    String.format("test_output/%s.minijava", CLASS_NAME)
            });
        }
        catch (SyntaxException ex) {
            System.out.println("Error at line : " + ex.getNode().ctx().start.getLine() + ":" + ex.getMessage());
        }
    }

    private static String getClassNameFromPath(String path) {
        Path p = Path.of(path);
        String filename = p.getFileName().toString();
        int index = filename.indexOf('.');
        return filename.substring(0, index);
    }
}
