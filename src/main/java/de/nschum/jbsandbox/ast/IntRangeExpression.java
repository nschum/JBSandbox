package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.SourceRange;

public class IntRangeExpression extends Expression {

    private Expression lowerBound;
    private Expression upperBound;

    public IntRangeExpression(Expression lowerBound, Expression upperBound, SourceRange location) {
        super(Type.INT_SEQUENCE, location);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public Expression getLowerBound() {
        return lowerBound;
    }

    public Expression getUpperBound() {
        return upperBound;
    }
}
