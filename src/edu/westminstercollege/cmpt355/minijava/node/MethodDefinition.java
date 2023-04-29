package edu.westminstercollege.cmpt355.minijava.node;

import edu.westminstercollege.cmpt355.minijava.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public record MethodDefinition(ParserRuleContext ctx, TypeNode returnType, String name, List<Parameter> parameters, Block block, SymbolTable symbolTable) implements Node {

    @Override
    public String getNodeDescription() {
        return "name";
    }

    public List<? extends Node> children() {
        List<Node> list = new ArrayList<>();
        list.add(returnType);
        list.addAll(parameters);
        list.add(block);
        return list;
    }

    @Override
    public void typecheck(SymbolTable symbols) throws SyntaxException {
        for(var parameter: parameters){
            parameter.typecheck(symbols);
        }
        block.typecheck(symbols);
    }

    @Override
    public void generateCode(PrintWriter out, SymbolTable symbols) {
        StringBuilder paramameters = new StringBuilder();
        for (var param : parameters) {
            if (param.type().type() == PrimitiveType.Int)
                paramameters.append("I");
            else if (param.type().type() == PrimitiveType.Double)
                paramameters.append("D");
            else if (param.type().type() == PrimitiveType.Boolean)
                paramameters.append("Z");
            else {
                paramameters.append("L");
                paramameters.append(symbols.classFromType(param.type().type()).orElseThrow().getName().replace('.', '/'));
                paramameters.append(";");
            }
        }

        String returm;
        if (returnType.type() == PrimitiveType.Int)
            returm = "I";
        else if (returnType.type() == PrimitiveType.Double)
            returm = "D";
        else if (returnType.type() == PrimitiveType.Boolean)
            returm = "Z";
        else if (returnType.type() instanceof VoidType)
            returm = "V";
        else
            returm = "L" + symbols.classFromType(returnType.type()).orElseThrow().getName().replace('.', '/') + ";";


        out.printf(".method public %s(%s)%s\n", name, paramameters, returm);
        out.println(".limit stack 100");
        out.printf(".limit locals %d\n", parameters.size() * 10 + 2);

        block.generateCode(out, symbols);

        if(returnType.type() instanceof VoidType) {
            out.println("return");
        }

        out.println(".end method\n");
    }

    public TypeNode getReturnType() {
        return returnType;
    }
}
