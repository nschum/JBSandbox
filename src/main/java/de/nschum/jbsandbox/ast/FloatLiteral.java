package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

public class FloatLiteral extends Expression {

    private double content;

    public FloatLiteral(double content, SourceRange location) {
        super(Type.FLOAT, location);
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
