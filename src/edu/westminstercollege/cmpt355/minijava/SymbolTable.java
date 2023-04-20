package edu.westminstercollege.cmpt355.minijava;

import java.util.*;

public class SymbolTable {

    private Map<String, Variable> variables = new HashMap<>();
    private int variableIndex = 0;

    public Variable registerVariable(String name, Type type) {
        Variable v = variables.get(name);
        if (v == null) {
            v = new Variable(name, type);
            variables.put(name, v);
        }

        return v;
    }

    public Optional<Variable> findVariable(String name) {
        return Optional.ofNullable(variables.get(name));
    }

    public int allocateLocalVariable(int size) {
        int index = variableIndex;
        variableIndex += size;
        return index;
    }

    public int getVariableCount() {
        return variableIndex;
    }

    public Optional<Class<?>> findJavaClass(String className) {
        return Reflect.classForName(className)
                .or(() -> Reflect.classForName("java.lang." + className))
                .or(() -> Reflect.classForName("java.util." + className));
    }

    public Optional<Class<?>> classFromType(Type type) {
        return switch (type) {
            case PrimitiveType pt -> Optional.of(switch (pt) {
                case Int -> Integer.TYPE;
                case Double -> Double.TYPE;
                case Boolean -> Boolean.TYPE;
            });

            case VoidType vt -> Optional.of(Void.TYPE);

            case ClassType ct -> findJavaClass(ct.getClassName());
        };
    }

    public Optional<List<Class<?>>> classesFromTypes(List<? extends Type> types) {
        List<Class<?>> classes = new ArrayList<>(types.size());
        for (var type : types) {
            var maybeClass = classFromType(type);
            if (maybeClass.isEmpty())
                return Optional.empty();
            classes.add(maybeClass.get());
        }
        return Optional.of(classes);
    }

    public Optional<Field> findField(ClassType classType, String fieldName) {
        return classFromType(classType)
                .flatMap(c -> Reflect.findField(c, fieldName));
    }

    public Optional<Method> findMethod(ClassType classType, String methodName, List<Type> parameterTypes) {
        var maybeParameterClasses = classesFromTypes(parameterTypes);
        if (maybeParameterClasses.isEmpty())
            return Optional.empty();
        return classFromType(classType)
                .flatMap(c -> Reflect.findMethod(c, methodName, maybeParameterClasses.get()));
    }

    public Optional<Method> findConstructor(ClassType classType, List<Type> parameterTypes) {
        var maybeParameterClasses = classesFromTypes(parameterTypes);
        if (maybeParameterClasses.isEmpty())
            return Optional.empty();
        return classFromType(classType)
                .flatMap(c -> Reflect.findConstructor(c, maybeParameterClasses.get()));
    }

}

