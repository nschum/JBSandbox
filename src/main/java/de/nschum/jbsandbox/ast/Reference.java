package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.SourceRange;

public class Reference extends Expression {

    private Variable variable;

    public Reference(Variable variable, SourceRange location) {
        super(variable.getType(), location);
        this.variable = variable;
    }

    public Variable getVariable() {
        return variable;
    }

    @Override
    protected String toString(String indent) {
        return indent + variable.toString();
    }
}
