package edu.westminstercollege.cmpt355.minijava;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SymbolTable {

    private Map<String, Variable> variables = new HashMap<>();

    private int index = 0;

    public Variable registerVariable(String name) {
        Variable v = variables.get(name);
        if (v == null) {
            v = new Variable(name, index);
            variables.put(name, v);
        }

        return v;
    }

    public Optional<Variable> findVariable(String name) {
        return Optional.ofNullable(variables.get(name));
    }

    public int allocateLocalVariable(int size) {
        return index += size;
    }

    public int getVariableCount() {
        return index;
    }
}
