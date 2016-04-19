package de.nschum.jbsandbox.interpreter;

/**
 * Holder for all values in the program state
 */
public class Value {

    private Object value;

    public Value(Object value) {
        this.value = value;
    }

    public Object get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
