package de.nschum.jbsandbox.ast;

public class MapExpression extends Expression {

    private Expression input;
    private Lambda function;

    public MapExpression(Type type, Expression input, Lambda function) {
        super(type);
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
