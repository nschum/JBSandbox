package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.SourceRange;

public abstract class Expression extends SyntaxTree {

    private Type type;

    public Expression(Type type, SourceRange location) {
        super(location);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
