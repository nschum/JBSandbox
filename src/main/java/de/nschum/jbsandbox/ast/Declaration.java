package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;

public class Declaration extends Statement {

    private Variable variable;
    private Expression expression;

    public Declaration(Variable variable, Expression expression, List<Terminal> terminals, SourceRange location) {
        super(terminals, location);
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
