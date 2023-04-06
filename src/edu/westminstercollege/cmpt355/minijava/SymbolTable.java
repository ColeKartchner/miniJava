package edu.westminstercollege.cmpt355.minijava;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SymbolTable {



    /*
    enum level{
        Class, Method, Block
        }

     private Level level;
     private SymbolTable parent;

     public SymnbolTable(Level level){
        this.level = level;
     }

     public Optional<Variable> findVariable(String name){

        var maybeVar = Optional.ofNullable(variables.get(name));
        var ancestor = parent;

        while (maybeVar.isEmpty()){
            if (ancestor != null) {
                maybeVar = parent.findVariable(name);
            }
            else{
                return Optional.empty();
            }
        }

        public int allocate Variable (int size) {
            // Only a Method-level Symbol Table can allocate a variable
            if (level == Level.Method) {
                // same code as before...
            }
            else if {
                return parent.allocateVariable(size);
            }
        }

     }
     */
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
