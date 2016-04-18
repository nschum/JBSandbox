package de.nschum.jbsandbox.ast;

import java.util.List;

public class Lambda {

    private List<Variable> parameters;
    private Expression expression;

    public Lambda(List<Variable> parameters, Expression expression) {
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
