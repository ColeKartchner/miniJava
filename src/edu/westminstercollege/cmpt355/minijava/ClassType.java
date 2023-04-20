package edu.westminstercollege.cmpt355.minijava;

import java.util.Objects;
import java.util.Optional;

public sealed class ClassType implements Type
    permits StaticType {

    public static final ClassType String = new ClassType("String");

    private final String className;

    public ClassType(String className) {
        if (className == null)
            throw new IllegalArgumentException("Class name cannot be null!");
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassType classType = (ClassType) o;
        return className.equals(classType.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className);
    }

    @Override
    public String toString() {
        return java.lang.String.format("ClassType[className=%s]", className);
    }
}
