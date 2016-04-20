package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

public class IntLiteral extends Expression {

    private int content;

    public IntLiteral(int content, SourceRange location) {
        super(Type.INT, location);
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
