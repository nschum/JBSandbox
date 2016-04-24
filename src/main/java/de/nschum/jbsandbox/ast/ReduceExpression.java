package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;

public class ReduceExpression extends Expression {

    private Expression input;
    private Expression initialValue;
    private Lambda function;

    public ReduceExpression(Type type, Expression input, Expression initialValue, Lambda function,
                            List<Terminal> terminals, SourceRange location) {
        super(type, terminals, location);
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
