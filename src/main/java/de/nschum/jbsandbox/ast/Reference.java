package de.nschum.jbsandbox.ast;

public class Reference extends Expression {

    private Variable variable;

    public Reference(Variable variable) {
        super(variable.getType());
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
