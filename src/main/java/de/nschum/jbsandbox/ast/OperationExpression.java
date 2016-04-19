package de.nschum.jbsandbox.ast;

public class OperationExpression extends Expression {

    private Expression leftHandSide;
    private Expression rightHandSide;
    private Operation operation;

    public OperationExpression(Type type, Expression leftHandSide, Expression rightHandSide, Operation operation) {
        super(type);
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
}