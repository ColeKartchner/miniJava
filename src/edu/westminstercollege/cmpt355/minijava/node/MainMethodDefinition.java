package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.SyntaxException;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;

public record MainMethodDefinition(ParserRuleContext ctx, Block block, SymbolTable symbolTable) implements Node {
    public List<? extends Node> children() {
        return List.of(block);
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        block.typecheck(symbols);
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        out.println(".method public main()V");
        out.printf(".limit stack 100\n");
        out.printf(".limit locals %d\n", symbols.getVariableCount() * 2 + 1);
        block.generateCode(out, symbols);
        out.println("return");
        out.println(".end method\n");
    }
}
