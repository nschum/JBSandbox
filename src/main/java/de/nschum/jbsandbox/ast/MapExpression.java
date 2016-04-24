package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;

public class MapExpression extends Expression {

    private Expression input;
    private Lambda function;

    public MapExpression(Type type, Expression input, Lambda function, List<Terminal> terminals, SourceRange location) {
        super(type, terminals, location);
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
