package de.nschum.jbsandbox.ast;

public class FloatLiteral extends Expression {

    private double content;

    public FloatLiteral(double content) {
        super(Type.FLOAT);
        this.content = content;
    }

    public double getContent() {
        return content;
    }

    @Override
    protected String toString(String indent) {
        return indent + content;
    }
}
