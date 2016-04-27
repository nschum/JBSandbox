package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;
import java.util.function.Consumer;

/**
 * A declaration is a statement that introduces a new variable and assigns it a value
 */
public class Declaration extends Statement {

    private final Variable variable;
    private final Expression expression;

    public Declaration(Variable variable, Expression expression, List<Terminal> terminals, SourceRange location) {
        super(terminals, location);
        this.variable = variable;
        this.expression = expression;
    }

    public Variable getVariable() {
        return variable;
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
        return indent + "var " + variable + ": " + variable.getType() + " = " + expression;
    }
}
