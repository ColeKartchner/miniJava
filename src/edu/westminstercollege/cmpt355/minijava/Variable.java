package edu.westminstercollege.cmpt355.minijava;

import edu.westminstercollege.cmpt355.minijava.node.Node;

public class Variable {
    private Type type;

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String toString() {
        return name + " " +type.toString();
    }
    private String name;
    private int index;

    public Variable(String name, int index) {
        this.type = type;
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}

