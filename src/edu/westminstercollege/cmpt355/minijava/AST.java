package edu.westminstercollege.cmpt355.minijava;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.westminstercollege.cmpt355.minijava.node.*;

public class AST {

    @FunctionalInterface
    public interface NodeConsumer<E extends Exception> {
        void consume(Node node) throws E;
    }

    public static void print(Node root) {
        print(root, new PrintWriter(System.out));
    }

    public static void print(Node root, PrintWriter out) {
        out.println(root.getNodeDescription());
        var it = root.children().iterator();
        while (it.hasNext())
            print(it.next(), out, List.of(it.hasNext()));
        out.flush();
    }

    private static void print(Node root, PrintWriter out, List<Boolean> levels) {
        for (boolean b : levels.subList(0, levels.size() - 1))
            out.printf("%3s", b ? "│ " : "");
        out.printf("%3s ", levels.get(levels.size() - 1) ? "├─" : "└─");

        String[] lines = root.getNodeDescription().split("\n");
        out.println(lines[0]);
        for (int i = 1; i < lines.length; ++i) {
            for (boolean b: levels)
                out.printf("%3s", b ? "│ " : "");
            out.printf(" %s\n", lines[i]);
        }

        var it = root.children().iterator();
        while (it.hasNext()) {
            var childLevels = new ArrayList<>(levels);
            var child = it.next();
            childLevels.add(it.hasNext());
            print(child, out, childLevels);
        }
    }

    public static <E extends Exception> void preOrder(Node root, NodeConsumer<E> c) throws E {
        c.consume(root);

        for (var child : root.children())
            preOrder(child, c);
    }

    public static <E extends Exception> void postOrder(Node root, NodeConsumer<E> c) throws E {
        for (var child : root.children())
            postOrder(child, c);

        c.consume(root);
    }
}
