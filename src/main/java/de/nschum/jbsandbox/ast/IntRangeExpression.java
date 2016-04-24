package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;

public class IntRangeExpression extends Expression {

    private Expression lowerBound;
    private Expression upperBound;

    public IntRangeExpression(Expression lowerBound, Expression upperBound, List<Terminal> terminals,
                              SourceRange location) {
        super(Type.INT_SEQUENCE, terminals, location);
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
