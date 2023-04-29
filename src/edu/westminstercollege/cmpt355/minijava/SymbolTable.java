package edu.westminstercollege.cmpt355.minijava;

import java.util.*;

/**
 * A class representing a symbol table, which stores information about symbols (classes, variables, methods) that occur
 * in a program.
 *
 * Symbol tables have levels: either class, method, or block, each keeping information about the sorts of symbols found
 * at that level. Specifically:
 *   - A class-level symbol table holds the fields and methods of a class, as well as what classes and packages are
 *     imported.
 *   - A method-level symbol table holds the parameters of the method and is responsible for assigning variables slots
 *     in the local variable array.
 *   - A block-level symbol table holds local variables defined within the block.
 *
 * In addition to its primary responsibilities, the SymbolTable class also knows the name of the class being compiled
 * and can make miniJava Types into their Java Class equivalents.
 */
public class SymbolTable {

    public enum Level {
        Class, Method, Block
    }

    private Level level;
    private SymbolTable parent = null;
    private Map<String, Variable> variables = new HashMap<>();
    private List<Method> methods = new ArrayList<>();
    private int variableIndex = 0;
    private String compilingClassName = null;

    private Map<String, Class<?>> importedClasses = new HashMap<>();
    private List<String> importedPackages = new ArrayList<>();

    /**
     * Creates a new symbol table with the given level (class, method, or block).
     */
    public SymbolTable(Level level) {
        if (level == null)
            throw new IllegalArgumentException("Level cannot be null!");
        this.level = level;
        importedPackages.add("java.lang");
    }

    /**
     * Returns the level of this symbol table, one of Level.Class, Level.Method, or Level.Block.
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Sets the name of the class being compiled. This is used in various other methods to determine whether a ClassType
     * represents the class being compiled.
     */
    public void setCompilingClassName(String compilingClassName) {
        this.compilingClassName = compilingClassName;
    }

    /**
     * Returns the name of the class being compiled, as previously set by setCompilingClassName(). (It the compiling
     * class name has not been set in this table, the parent table is queried.)
     */
    public String getCompilingClassName() {
        if (compilingClassName == null && parent != null)
            return parent.getCompilingClassName();
        return compilingClassName;
    }

    /**
     * Sets the parent of this symbol table. Any requested symbols that are not found in this table are then searched in
     * the parent table if present.
     */
    public void setParent(SymbolTable parent) {
        this.parent = parent;
    }

    /**
     * Returns the parent of this symbol table, if present.
     */
    public Optional<SymbolTable> getParent() {
        return Optional.ofNullable(parent);
    }

    /**
     * Registers a new variable of the given name and type, so that later calls to findVariable() with the specified
     * name will locate it. Returns the created Variable.
     */
    public Variable registerVariable(String name, Type type) {
        Variable v = variables.get(name);
        if (v == null) {
            v = new Variable(name, type);
            variables.put(name, v);
        }

        return v;
    }

    /**
     * Registers a new field of the given name and type, so that later calls to findVariable() with the specified
     * name will locate it. Returns the created Variable.
     * This method is identical to registerVariable() except that it also calls setField(true) on the Variable.
     */
    public Variable registerField(String name, Type type) {
        Variable v = variables.get(name);
        if (v == null) {
            v = new Variable(name, type);
            variables.put(name, v);
        }
        v.setField(true);

        return v;
    }

    /**
     * Allocates space in the local variable array for a variable of the given size and returns its index. (This method
     * will pass the allocation request up to its parent unless the table is of Class level.)
     */
    public int allocateLocalVariable(int size) {
        if (level == Level.Method) {
            int index = variableIndex;
            variableIndex += size;
            return index;
        } else if (parent != null)
            return parent.allocateLocalVariable(size);
        else
            throw new RuntimeException("Internal compiler error: cannot allocate local variable at level " + level);
    }

    /**
     * Registers a new method with the given name, parameter types, and return types, so that later calls to
     * findMethod() for the class being compiled will find it.
     */
    public Method registerMethod(String name, List<Type> parameterTypes, Type returnType) {
        var method = new Method(new ClassType(getCompilingClassName()), name, parameterTypes, returnType);
        methods.add(method);
        return method;
    }

    /**
     * Imports a single specified class. For example, if the Class corresponding to java.util.Scanner is specified, then
     * a subsequent call to findJavaClass("Scanner") will return it.
     */
    public void importClass(Class<?> clazz) {
        importedClasses.put(clazz.getSimpleName(), clazz);
    }

    /**
     * Imports a package whose name is given in "dotted names" form. For example, if the package "java.util" is
     * specified, then a subsequent call to findJavaClass("List") will return the Class for java.util.List.
     * The java.lang package is automatically imported.
     */
    public void importPackage(String packageName) {
        importedPackages.add(packageName);
    }

    /**
     * Returns the number of local variables allocated in this method.
     */
    public int getVariableCount() {
        return variableIndex;
    }

    /**
     * Returns the registered variable with the given name in this table or a parent, or an empty Optional if none is
     * found.
     */
    public Optional<Variable> findVariable(String name) {
        var maybeVariable = Optional.ofNullable(variables.get(name));
        if (maybeVariable.isEmpty() && parent != null)
            return parent.findVariable(name);
        return maybeVariable;
    }

    /**
     * Returns the Java class of the specified name, or an empty Optional if not found. This method refers to any
     * previously imported classes or packages (from the importClass()/importPackage() methods). (The java.lang package
     * is automatically imported.)
     */
    public Optional<Class<?>> findJavaClass(String className) {
        if (importedClasses.containsKey(className))
            return Optional.of(importedClasses.get(className));

        var maybeClass = Reflect.classForName(className);

        if (maybeClass.isEmpty() && !className.contains(".")) {
            for (var packageName : importedPackages) {
                maybeClass = Reflect.classForName(packageName + "." + className);
                if (maybeClass.isPresent())
                    break;
            }
        }

        if (maybeClass.isEmpty() && parent != null)
            return parent.findJavaClass(className);

        return maybeClass;
    }

    /**
     * Finds a field in the given ClassType and returns the corresponding Field. If classType represents the class being
     * compiled, any fields registered in this table are checked; otherwise, the Java Reflection API is used.
     */
    public Optional<Field> findField(ClassType classType, String fieldName) {
        if (classType.getClassName().equals(getCompilingClassName())) {
            return findLocalField(fieldName);
        }
        var maybeField = classFromType(classType)
                .flatMap(c -> Reflect.findField(c, fieldName));
        if (maybeField.isEmpty() && parent != null)
            return parent.findField(classType, fieldName);
        return maybeField;
    }

    /**
     * Finds a method in the given ClassType and returns the corresponding Method. If classType represents the class
     * being compiled, any methods registered in this table are checked; otherwise, the Java Reflection API is used.
     */
    public Optional<Method> findMethod(ClassType classType, String methodName, List<Type> parameterTypes) {
        if (classType.getClassName().equals(getCompilingClassName())) {
            return findLocalMethod(methodName, parameterTypes);
        }

        var maybeParameterClasses = classesFromTypes(parameterTypes);
        if (maybeParameterClasses.isEmpty())
            return Optional.empty();

        var maybeMethod = classFromType(classType)
                .flatMap(c -> Reflect.findMethod(c, methodName, maybeParameterClasses.get()));
        if (maybeMethod.isEmpty() && parent != null)
            return parent.findMethod(classType, methodName, parameterTypes);
        return maybeMethod;
    }

    /**
     * Finds a constructor in the given ClassType and returns the corresponding Method. If classType represents the
     * class being compiled and parameterTypes is empty, returns a Method signifying the default constructor; any other
     * parmaeterTypes on the class being compiled results in an empty Optional, since miniJava classes have no non-
     * default constructors. If classType is not the class being compiled, the Java Reflection API is used.
     */
    public Optional<Method> findConstructor(ClassType classType, List<Type> parameterTypes) {
        if (classType.getClassName().equals(getCompilingClassName())) {
            if (parameterTypes.isEmpty())
                return Optional.of(new Method(classType, "<init>", List.of(), VoidType.Instance));
            return Optional.empty();
        }

        var maybeParameterClasses = classesFromTypes(parameterTypes);
        if (maybeParameterClasses.isEmpty())
            return Optional.empty();
        var maybeCtor = classFromType(classType)
                .flatMap(c -> Reflect.findConstructor(c, maybeParameterClasses.get()));
        if (maybeCtor.isEmpty() && parent != null)
            return parent.findConstructor(classType, parameterTypes);
        return maybeCtor;
    }

    /**
     * Returns the Java class corresponding to the given Type, or an empty Optional if there is no corresponding class.
     */
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

    /**
     * Returns a list of Java classes corresponding to the given Types. If <em>any</em> of the types given does not
     * have a corresponding class (as determined by classFromType()), an empty Optional is returned.
     */
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

    private Optional<Method> findLocalMethod(String name, List<Type> parameterTypes) {
        if (level != Level.Class)
            return parent.findLocalMethod(name, parameterTypes);

        var maybeMethod = methods.stream()
                .filter(m -> m.name().equals(name))
                .filter(m -> m.parameterTypes().equals(parameterTypes))
                .findAny();

        return maybeMethod;
    }

    private Optional<Field> findLocalField(String name) {
        if (level != Level.Class)
            return parent.findLocalField(name);

        var maybeVar = findVariable(name);
        if (maybeVar.isPresent() && maybeVar.get().isField())
            return Optional.of(new Field(new ClassType(getCompilingClassName()), name, maybeVar.get().getType()));
        return Optional.empty();
    }
}

