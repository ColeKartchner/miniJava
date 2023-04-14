package edu.westminstercollege.cmpt355.minijava;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
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

    public Optional<Class<?>> findJavaClass(String className) {
        var clazz = Reflect.classForName(className);
        if(clazz.isEmpty()){
            clazz = Reflect.classForName(String.format("java.lang." + className));
            if(clazz.isEmpty()){
                clazz = Reflect.classForName(String.format("java.util." + className));
                if(clazz.isEmpty()){
                    return Optional.empty();
                }
                else {
                    return clazz;
                }
            }
            else {
                return clazz;
            }
        }
        else {
            return clazz;
        }
    }

    public Optional<Class<?>> classFromType(Type type){
        if (type.equals(PrimitiveType.Int))
            return Optional.of(Integer.TYPE);
        else if (type.equals(PrimitiveType.Double))
            return Optional.of(Double.TYPE);
        else if (type.equals(PrimitiveType.Boolean))
            return Optional.of(Boolean.TYPE);
        else if (type.equals(VoidType.Instance))
            return Optional.of(Void.TYPE);
        else if (type instanceof ClassType clazz)
            return findJavaClass(clazz.getClassName());
        else
            return Optional.empty();
    }

    public Optional<Field> findField(ClassType classType, String fieldName) {
        var clazz = classFromType(classType);
        if (clazz.isPresent())
            return Reflect.findField(clazz.get(), fieldName);
        else
            return Optional.empty();
    }

    public Optional<Method> findMethod(ClassType classType, String methodName, List<Type> parameterTypes){
        var clazz = classFromType(classType);
        List<Class<?>> typeParams = new ArrayList<>();
        for (var parameterType : parameterTypes) {
            var clazzParameterType = classFromType(parameterType);
            if (clazzParameterType.isPresent())
                typeParams.add(clazzParameterType.get());
        }
        if (clazz.isPresent() && typeParams.size() == parameterTypes.size())
            return Reflect.findMethod(clazz.get(), methodName, typeParams);
        else
            return Optional.empty();
    }

    public Optional<Method> findConstructor(ClassType classType, List<Type> parameterTypes){
        var clazz = classFromType(classType);
        List<Class<?>> clazzParameterTypes = new ArrayList<>();
        for (var parameterType : parameterTypes) {
            var clazzParameterType = classFromType(parameterType);
            if (clazzParameterType.isPresent())
                clazzParameterTypes.add(clazzParameterType.get());
        }
        if (clazz.isPresent() && clazzParameterTypes.size() == parameterTypes.size())
            return Reflect.findConstructor(clazz.get(), clazzParameterTypes);
        else
            return Optional.empty();
    }
}

