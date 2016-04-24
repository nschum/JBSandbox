package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;

public class Lambda extends SyntaxTree {

    private List<Variable> parameters;
    private Expression expression;

    public Lambda(List<Variable> parameters, Expression expression, List<Terminal> terminals, SourceRange location) {
        super(terminals, location);
        this.parameters = parameters;
        this.expression = expression;
    }

    public List<Variable> getParameters() {
        return parameters;
    }

    public Expression getExpression() {
        return expression;
    }
}
