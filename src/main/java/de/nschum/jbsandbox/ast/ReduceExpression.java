package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

public class ReduceExpression extends Expression {

    private Expression input;
    private Expression initialValue;
    private Lambda function;

    public ReduceExpression(Type type, Expression input, Expression initialValue, Lambda function,
                            SourceRange location) {
        super(type, location);
        this.input = input;
        this.initialValue = initialValue;
        this.function = function;
    }

    public Expression getInput() {
        return input;
    }

    public Expression getInitialValue() {
        return initialValue;
    }

    public Lambda getFunction() {
        return function;
    }
}
