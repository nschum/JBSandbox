package de.nschum.jbsandbox.interpreter;

import de.nschum.jbsandbox.ast.Variable;
import de.nschum.jbsandbox.util.TinyMap;

import java.util.HashMap;
import java.util.Map;

/**
 * The program's state
 */
public class State {

    private Map<Variable, Value> values = new TinyMap<>();

    public State() {
    }

    private State(State parent) {
        values.putAll(parent.values);
    }

    /**
     * Read the current value of a variable
     */
    public Value lookUp(Variable variable) {
        assert variable != null;
        Value value = values.get(variable);
        if (value != null) {
            return value;
        }
        throw new IllegalArgumentException("No such variable: " + variable);
    }

    /**
     * Store a new value of a variable or a new variable
     */
    public void store(Variable variable, Value value) {
        try {
            values.put(variable, value);
        } catch (TinyMap.TinyMapLimitExceededException e) {
            // TinyMap only stores up to 2 values, which is fast for the common case.
            // Switch to full hash map now.
            values = new HashMap<>(values);
            values.put(variable, value);
        }
    }

    /**
     * Creates a new scope
     * <p/>
     * Values from this scope are readable in the new scope.
     * Values added to the new scope do not affect this scope.
     */
    public State openNewScope() {
        return new State(this);
    }
}
