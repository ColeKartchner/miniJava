package edu.westminstercollege.cmpt355.minijava;

import java.lang.reflect.Executable;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Reflect {

    /**
     * Converts a Java type (in a java.lang.Class object) to a miniType (instance of the Type interface).
     * Returns an empty Optional if there is no corresponding miniJava type (for example, there is no miniJava type
     * corresponding to a long).
     */
    public static Optional<Type> typeFromClass(Class<?> c) {
        if (c.equals(Integer.TYPE))
            return Optional.of(PrimitiveType.Int);
        else if (c.equals(Double.TYPE))
            return Optional.of(PrimitiveType.Double);
        else if (c.equals(Boolean.TYPE))
            return Optional.of(PrimitiveType.Boolean);
        else if (c.equals(Void.TYPE))
            return Optional.of(VoidType.Instance);
        else if (!c.isPrimitive() && !c.isArray())
            return Optional.of(new ClassType(c.getName()));
        else
            return Optional.empty();
    }

    /**
     * Returns an Optional containing the class corresponding to the given name, or an empty Optional if there is no
     * class with that name. (The name should include the package, e.g. "java.lang.String", not just "String".)
     * This method is just a thin wrapper around Class.forName() that returns an Optional instead of throwing an
     * exception.
     */
    public static Optional<Class<?>> classForName(String name) {
        try {
            return Optional.of(Class.forName(name));
        } catch (ClassNotFoundException ex) {
            return Optional.empty();
        }
    }

    /**
     * Returns a Field object corresponding to the given field of the given class, or an empty Optional if there is no
     * such field.
     */
    public static Optional<Field> findField(Class<?> clazz, String name) {
        try {
            var jField = clazz.getField(name);
            var fieldType = typeFromClass(jField.getType());
            if (fieldType.isPresent()) {
                if ((jField.getModifiers() & Modifier.STATIC) == 0)
                    return Optional.of(new Field(new ClassType(clazz.getName()), name, fieldType.get()));
                else
                    return Optional.of(new Field(new StaticType(clazz.getName()), name, fieldType.get()));
            }
        } catch (NoSuchFieldException ex) {
            // do nothing
        }

        return Optional.empty();
    }

    /**
     * Returns a Method object corresponding to the relevant method of the given class, or an empty Optional if there is
     * no such method. The returned method, if there is one, will meet the following requirements:
     * - belongs to the given class;
     * - has the given name;
     * - has the parameters whose types exactly correspond to those given;
     * - 
     * @param clazz
     * @param name
     * @param parameterTypes
     * @return
     */
    public static Optional<Method> findMethod(Class<?> clazz, String name, List<Class<?>> parameterTypes) {
        var methods = Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().equals(name))
                .filter(m -> Reflect.typeFromClass(m.getReturnType()).isPresent())
                .toList();
        var maybeMethod = filterCallable(methods, parameterTypes);

        if (maybeMethod.isPresent()) {
            var m = maybeMethod.get();
            var returnType = Reflect.typeFromClass(m.getReturnType()).get();
            var paramTypes = Arrays.stream(m.getParameterTypes())
                    .map(Reflect::typeFromClass)
                    .map(Optional::get)
                    .toList();
            if ((m.getModifiers() & Modifier.STATIC) == 0)
                return Optional.of(new Method(new ClassType(clazz.getName()), m.getName(), paramTypes, returnType));
            else
                return Optional.of(new Method(new StaticType(clazz.getName()), m.getName(), paramTypes, returnType));
        } else
            return Optional.empty();
    }

    public static Optional<Method> findConstructor(Class<?> clazz, List<Class<?>> parameterTypes) {
        var ctors = Arrays.asList(clazz.getConstructors());
        var maybeCtor = filterCallable(ctors, parameterTypes);

        if (maybeCtor.isPresent()) {
            var c = maybeCtor.get();
            var paramTypes = Arrays.stream(c.getParameterTypes())
                    .map(Reflect::typeFromClass)
                    .map(Optional::get)
                    .toList();
            return Optional.of(new Method(new ClassType(clazz.getName()), "<init>", paramTypes, VoidType.Instance));
        } else
            return Optional.empty();
    }

    private static <E extends Executable> Optional<E> filterCallable(Iterable<E> candidates, List<Class<?>> parameterTypes) {
        candidateLoop:
        for (var candidate : candidates) {
            if (candidate.getParameterCount() != parameterTypes.size())
                continue candidateLoop;

            var candidateParamTypes = candidate.getParameterTypes();
            for (int i = 0; i < parameterTypes.size(); ++i) {
                if (!parameterTypes.get(i).equals(candidateParamTypes[i]))
                    continue candidateLoop;
            }

            return Optional.of(candidate);
        }

        return Optional.empty();
    }

}
