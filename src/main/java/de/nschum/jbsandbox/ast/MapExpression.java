package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.SourceRange;

public class MapExpression extends Expression {

    private Expression input;
    private Lambda function;

    public MapExpression(Type type, Expression input, Lambda function, SourceRange location) {
        super(type, location);
        this.input = input;
        this.function = function;
    }

    public Expression getInput() {
        return input;
    }

    public Lambda getFunction() {
        return function;
    }
}
