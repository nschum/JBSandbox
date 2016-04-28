package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;
import java.util.function.Consumer;

/**
 * Statement that outputs a variable
 */
public class OutStatement extends Statement {

    private final Expression expression;

    public OutStatement(Expression expression, List<Terminal> terminals, SourceRange location) {
        super(terminals, location);
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public void visit(Consumer<SyntaxTree> visitor) {
        super.visit(visitor);
        expression.visit(visitor);
    }

    @Override
    protected String toString(String indent) {
        return indent + "out " + expression + "";
    }
}
