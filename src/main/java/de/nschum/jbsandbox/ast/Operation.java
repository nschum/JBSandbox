package de.nschum.jbsandbox.ast;

public enum Operation {
    PLUS(10),
    MINUS(10),
    MULTIPLY(20),
    DIVIDE(20),
    EXP(30);

    /**
     * The operation's precedence
     * <p>
     * A higher precedence means the operation is executed first.
     */
    private final int precedence;

    Operation(int precedence) {
        this.precedence = precedence;
    }

    public boolean hasHigherPrecedenceThan(Operation operation) {
        return precedence > operation.precedence;
    }
}
