package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;

public class Reference extends Expression {

    private Variable variable;

    public Reference(Variable variable, List<Terminal> terminals, SourceRange location) {
        super(variable.getType(), terminals, location);
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
