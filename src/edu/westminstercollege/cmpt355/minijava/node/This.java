// Parser rule:
// expression :
//   ...
//   | 'this' {
//       $n = new This($ctx);
//   }

package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.SyntaxException;
import edu.westminstercollege.cmpt355.minijava.Type;
import org.antlr.v4.runtime.ParserRuleContext;
import edu.westminstercollege.cmpt355.minijava.ClassType;

import java.io.PrintWriter;
import java.util.List;

public record This(ParserRuleContext ctx) implements Expression {

    @Override
    public List<Node> children() {
        return List.of();
    }

    @Override
    public Type getType(SymbolTable symbols) {
        return new ClassType(symbols.getCompilingClassName());
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        symbols.allocateLocalVariable(1);
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        out.println("aload_0");
    }
}
