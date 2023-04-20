package edu.westminstercollege.cmpt355.minijava;

public final class StaticType extends ClassType {

    public StaticType(java.lang.String className) {
        super(className);
    }

    @Override
    public String toString() {
        return java.lang.String.format("StaticType[className=%s]", getClassName());
    }
}
