package de.nschum.jbsandbox.ast;

/**
 * Single expression node
 * <p>
 * This is used to stop OperatorPrecedenceSyntoxTreeRewriter from rewriting operations when it reaches parentheses.
 */
public class ParenthesizedExpression extends Expression {

    private Expression expression;

    public ParenthesizedExpression(Expression expression) {
        super(expression.getType());
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    protected String toString(String indent) {
        return expression.toString(indent);
    }
}
