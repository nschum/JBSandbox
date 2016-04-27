package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;
import java.util.function.Consumer;

/**
 * An expression that reduces a sequence to a single value by iterating over it and combining the values
 */
public class ReduceExpression extends Expression {

    private final Expression input;
    private final Expression initialValue;
    private final Lambda function;

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

    @Override
    public void visit(Consumer<SyntaxTree> visitor) {
        super.visit(visitor);
        input.visit(visitor);
        initialValue.visit(visitor);
        function.visit(visitor);
    }
}
