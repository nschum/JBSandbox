package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;
import java.util.function.Consumer;

/**
 * An expression that applies an operation to two operands
 */
public class OperationExpression extends Expression {

    private final Expression leftHandSide;
    private final Expression rightHandSide;
    private final Operation operation;

    public OperationExpression(Type type, Expression leftHandSide, Expression rightHandSide, Operation operation,
                               List<Terminal> terminals) {
        super(type, terminals,
                new SourceRange(leftHandSide.getLocation().getStart(), rightHandSide.getLocation().getEnd()));
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
        this.operation = operation;
    }

    public Expression getLeftHandSide() {
        return leftHandSide;
    }

    public Expression getRightHandSide() {
        return rightHandSide;
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    protected String toString(String indent) {
        return indent
                + "(" + leftHandSide.toString() + ") "
                + operation.toString() +
                " (" + rightHandSide.toString() + ")";
    }

    @Override
    public void visit(Consumer<SyntaxTree> visitor) {
        super.visit(visitor);
        leftHandSide.visit(visitor);
        rightHandSide.visit(visitor);
    }
}
