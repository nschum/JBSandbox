package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;
import java.util.function.Consumer;

/**
 * An expression that maps a sequence to another sequence by applying a function to each item
 */
public class MapExpression extends Expression {

    private final Expression input;
    private final Lambda function;

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

    @Override
    public void visit(Consumer<SyntaxTree> visitor) {
        super.visit(visitor);
        input.visit(visitor);
        function.visit(visitor);
    }
}
