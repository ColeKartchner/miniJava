package edu.westminstercollege.cmpt355.minijava.node;
import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.SyntaxException;
import org.antlr.v4.runtime.ParserRuleContext;
import java.util.List;

public record PackageImport(ParserRuleContext ctx, String path) implements Import {
    @Override
    public ParserRuleContext ctx() {
        return ctx;
    }
    @Override
    public List<? extends Node> children() {
        return List.of();
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        symbols.importPackage(path);
    }
    public String getNodeDescription() {
        return String.format("ClassImport: [%s]", path);
    }
}
