package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.SourceRange;

public class OutStatement extends Statement {

    private Expression expression;

    public OutStatement(Expression expression, SourceRange location) {
        super(location);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    protected String toString(String indent) {
        return indent + "out " + expression + "";
    }
}
