package de.nschum.jbsandbox.ast;

/**
 * A handle for variables used in Declarations, References and Lambdas
 * <p/>
 * While variables are assigned a value on execution, these handles are immutable.
 */
public class Variable {

    private final Type type;
    private final String name;

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
