package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.Collections;

public class IntLiteral extends Expression {

    private int content;

    public IntLiteral(int content, SourceRange location) {
        super(Type.INT, Collections.emptyList(), location);
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
