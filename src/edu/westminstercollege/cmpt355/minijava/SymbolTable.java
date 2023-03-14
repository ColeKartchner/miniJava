package edu.westminstercollege.cmpt355.minijava;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SymbolTable {

    private Map<String, Variable> variables = new HashMap<>();
    private int variableIndex = 1;

    public Variable registerVariable(String name) {
        Variable v = variables.get(name);
        if (v == null) {
            v = new Variable(name, variableIndex);
            variableIndex += 2;
            variables.put(name, v);
        }

        return v;
    }

    public Optional<Variable> findVariable(String name) {
        return Optional.ofNullable(variables.get(name));
    }

    public int getVariableCount() {
        return variables.size();
    }
}
