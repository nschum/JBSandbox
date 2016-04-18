package de.nschum.jbsandbox.ast;

public class OutStatement extends Statement {

    private Expression expression;

    public OutStatement(Expression expression) {
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
