package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;

/**
 * Single expression node
 * <p/>
 * This is used to stop OperatorPrecedenceSyntoxTreeRewriter from rewriting operations when it reaches parentheses.
 */
public class ParenthesizedExpression extends Expression {

    private Expression expression;

    public ParenthesizedExpression(Expression expression, List<Terminal> terminals, SourceRange location) {
        super(expression.getType(), terminals, location);
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
