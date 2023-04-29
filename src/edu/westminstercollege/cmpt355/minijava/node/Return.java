// Parser rule:
// statement :
//   ....
//   | 'return' ';' {
//       $n = new Return($ctx, Optional.empty());
//   }
//   | 'return' e=expression ';' {
//       $n = new Return($ctx, Optional.of($e.n));
//   }

package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

public record Return(ParserRuleContext ctx, Optional<Expression> value) implements Statement {

    @Override
    public List<Node> children() {
        if (value.isPresent())
            return List.of(value.get());
        else
            return List.of();
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        // Not worrying about it
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        if (value.isPresent())
            value.orElseThrow().generateCode(out, symbols);

        if (value.isEmpty()) {
            out.println("return");
            return;
        }

        Type value_type = value.orElseThrow().getType(symbols);

        if (value_type == PrimitiveType.Int ||value_type == PrimitiveType.Boolean)
            out.println("ireturn");
        else if (value_type == PrimitiveType.Double)
            out.println("dreturn");
        else if (value_type instanceof ClassType)
            out.println("areturn");

    }
}
