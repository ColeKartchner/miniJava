package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public record VariableDeclarations(ParserRuleContext ctx, TypeNode type, List<DeclarationItem> items) implements Statement {

    @Override
    public List<? extends Node> children() {
        var children = new ArrayList<Node>();
        children.add(type);
        children.addAll(items);
        return children;
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        int varSize = (type.type() == PrimitiveType.Double) ? 2 : 1;
        var declType = type.type();

        if (declType instanceof ClassType ct) {
            var maybeClass = symbols.findJavaClass(ct.getClassName());
            if (maybeClass.isEmpty())
                throw new SyntaxException(this, String.format("Unknown class: %s", ct.getClassName()));
        }

        for (var item : items) {
            if (item.initializer().isPresent()) {
                var init = item.initializer().get();
                init.typecheck(symbols);
                Type initType = init.getType(symbols);

                if (type.type() == PrimitiveType.Double) {
                    if (initType != PrimitiveType.Int && initType != PrimitiveType.Double)
                        throw new SyntaxException(item, String.format("Cannot initialize variable of type double with value of type %s", initType));
                }

                else if (type.type() instanceof ClassType tct && initType instanceof ClassType ict) {
                    var tClass = symbols.findJavaClass(tct.getClassName()).get();
                    var maybeIClass = symbols.findJavaClass(ict.getClassName());
                    if (maybeIClass.isEmpty())
                        throw new SyntaxException(item, String.format("Invalid initialization of unknown class type %s", ict.getClassName()));
                    if (!maybeIClass.get().equals(tClass))
                        throw new SyntaxException(item, String.format("Cannot initialize variable of type %s with value of type %s", tct.getClassName(), ict.getClassName()));
                }

                else if (!type.type().equals(initType))
                    throw new SyntaxException(item, String.format("Cannot initialize variable of type %s with value of type %s", type.type(), initType));
            }

            var variable = symbols.findVariable(item.name()).get();
            variable.setIndex(symbols.allocateLocalVariable(varSize));
        }
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        var varType = type.type();
        for (var item : items) {
            if (item.initializer().isPresent()) {
                var itemInit = item.initializer().get();
                var itemInitType = itemInit.getType(symbols);
                itemInit.generateCode(out, symbols);

                var var = symbols.findVariable(item.name()).get();

                if (varType == PrimitiveType.Double && itemInitType == PrimitiveType.Int)
                    out.println("i2d");

                if (varType == PrimitiveType.Int || varType == PrimitiveType.Boolean)
                    out.printf("istore %d\n", var.getIndex());
                else if (varType == PrimitiveType.Double)
                    out.printf("dstore %d\n", var.getIndex());
                else if (varType instanceof ClassType)
                    out.printf("astore %d\n", var.getIndex());
                else
                    throw new RuntimeException(String.format("Internal compiler error: initialization of variable of type %s", varType));
            }
        }
    }
}
