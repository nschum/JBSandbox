package de.nschum.jbsandbox.ast;

public class IntRangeExpression extends Expression {

    private Expression lowerBound;
    private Expression upperBound;

    public IntRangeExpression(Expression lowerBound, Expression upperBound) {
        super(Type.INT_SEQUENCE);
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
