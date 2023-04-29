package edu.westminstercollege.cmpt355.minijava.node;
import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.SyntaxException;
import org.antlr.v4.runtime.ParserRuleContext;
import java.util.List;

public record ClassImport(ParserRuleContext ctx, List<String> importParts) implements Import {

    @Override
    public List<? extends Node> children() {
        return List.of();
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        String path = "";
        for (int i = 0; i < importParts.size(); i++) {
            path = path.concat(importParts.get(i));
            if (i < importParts.size() - 1) {
                path = path.concat(".");
            }
        }
        var clazz = symbols.findJavaClass(path);
        if(clazz.isPresent()) {
            symbols.importClass(clazz.get());
        } else {
            throw new SyntaxException(String.format("No class import named: %s", path));
        }
    }

    public String getNodeDescription() {
        String inport = importParts.toString();
        return String.format("ClassImport: [%s]", inport);
    }

    @Override
    public ParserRuleContext ctx() {
        return ctx;
    }
}
