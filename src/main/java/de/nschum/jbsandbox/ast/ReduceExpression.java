package de.nschum.jbsandbox.ast;

public class ReduceExpression extends Expression {

    private Expression input;
    private Expression initialValue;
    private Lambda function;

    public ReduceExpression(Type type, Expression input, Expression initialValue, Lambda function) {
        super(type);
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
