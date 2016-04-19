package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.SourceRange;

public class Declaration extends Statement {

    private Variable variable;
    private Expression expression;

    public Declaration(Variable variable, Expression expression, SourceRange location) {
        super(location);
        this.variable = variable;
        this.expression = expression;
    }

    public Variable getVariable() {
        return variable;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    protected String toString(String indent) {
        return indent + "var " + variable + ": " + variable.getType() + " = " + expression;
    }
}
