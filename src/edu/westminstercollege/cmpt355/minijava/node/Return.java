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

import edu.westminstercollege.cmpt355.minijava.SymbolTable;
import edu.westminstercollege.cmpt355.minijava.SyntaxException;
import org.antlr.v4.runtime.ParserRuleContext;

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

    }
}
