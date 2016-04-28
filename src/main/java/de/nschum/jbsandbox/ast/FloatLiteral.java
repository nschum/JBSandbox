package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.Collections;

/**
 * A literal value of type FLOAT
 */
public class FloatLiteral extends Expression {

    private final double content;

    public FloatLiteral(double content, SourceRange location) {
        super(Type.FLOAT, Collections.emptyList(), location);
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
