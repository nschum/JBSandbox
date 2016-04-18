package de.nschum.jbsandbox.ast;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A scope of bound variables inside the syntax tree.
 */
public class Scope {

    private Optional<Scope> parent;
    private Map<String, Variable> variables = new HashMap<>();

    public Scope() {
        parent = Optional.empty();
    }

    private Scope(Scope parent) {
        this.parent = Optional.of(parent);
    }

    public Optional<Variable> lookUp(String name) {
        assert name != null;

        final Variable variable = variables.get(name);
        if (variable != null) {
            return Optional.of(variable);
        }
        if (parent.isPresent()) {
            return parent.get().lookUp(name);
        }
        return Optional.empty();
    }

    Scope addVariable(Variable variable) {
        assert variable != null;

        final Scope scope = new Scope(this);
        scope.variables.put(variable.getName(), variable);
        return scope;
    }
}
