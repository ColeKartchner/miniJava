package edu.westminstercollege.cmpt355.minijava;

import java.util.List;

public record Method(ClassType containingType, String name, List<Type> parameterTypes, Type returnType) {

}
