package de.nschum.jbsandbox.ast;

public abstract class Expression extends SyntaxTree {

    private Type type;

    public Expression(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
