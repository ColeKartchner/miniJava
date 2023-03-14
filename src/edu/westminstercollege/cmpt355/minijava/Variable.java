package edu.westminstercollege.cmpt355.minijava;

public class Variable {

    private String name;
    private int index;

    public Variable(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

}
