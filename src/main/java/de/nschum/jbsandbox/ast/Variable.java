package de.nschum.jbsandbox.ast;

public class Variable {

    private Type type;
    private String name;

    public Variable(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
