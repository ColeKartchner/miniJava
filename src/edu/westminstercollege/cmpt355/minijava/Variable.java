package edu.westminstercollege.cmpt355.minijava;

public class Variable {

    private String name;
    private Type type;
    private int index;
    private boolean isField;

    public Variable(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return String.format("Variable [name=%s, type=%s]", name, type);
    }

    public void setField(boolean isField) {
        this.isField = isField;
    }
    public boolean isField() {
        return isField;
    }
}
