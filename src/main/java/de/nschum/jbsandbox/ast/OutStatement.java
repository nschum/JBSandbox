package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;

public class OutStatement extends Statement {

    private Expression expression;

    public OutStatement(Expression expression, List<Terminal> terminals, SourceRange location) {
        super(terminals, location);
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
