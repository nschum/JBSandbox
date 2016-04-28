package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;
import java.util.function.Consumer;

/**
 * A literal value of type INT[] containing sequential values
 */
public class IntRangeExpression extends Expression {

    private final Expression lowerBound;
    private final Expression upperBound;

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

    @Override
    public void visit(Consumer<SyntaxTree> visitor) {
        super.visit(visitor);
        lowerBound.visit(visitor);
        upperBound.visit(visitor);
    }
}
