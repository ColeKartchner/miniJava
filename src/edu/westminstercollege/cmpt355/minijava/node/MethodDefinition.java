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
        StringBuilder param_string = new StringBuilder();
        for (var param : parameters) {
            if (param.type().type() == PrimitiveType.Int)
                param_string.append("I");
            else if (param.type().type() == PrimitiveType.Double)
                param_string.append("D");
            else if (param.type().type() == PrimitiveType.Boolean)
                param_string.append("Z");
            else {
                param_string.append("L");
                param_string.append(symbols.classFromType(param.type().type()).orElseThrow().getName().replace('.', '/'));
                param_string.append(";");
            }
        }

        String return_type_string;
        if (returnType.type() == PrimitiveType.Int)
            return_type_string = "I";
        else if (returnType.type() == PrimitiveType.Double)
            return_type_string = "D";
        else if (returnType.type() == PrimitiveType.Boolean)
            return_type_string = "Z";
        else if (returnType.type() instanceof VoidType)
            return_type_string = "V";
        else
            return_type_string = "L" + symbols.classFromType(returnType.type()).orElseThrow().getName().replace('.', '/') + ";";


        out.printf(".method public %s(%s)%s\n", name, param_string, return_type_string);
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
