package de.nschum.jbsandbox.ast;

public class IntLiteral extends Expression {

    private int content;

    public IntLiteral(int content) {
        super(Type.INT);
        this.content = content;
    }

    public int getContent() {
        return content;
    }

    @Override
    protected String toString(String indent) {
        return indent + content;
    }
}
