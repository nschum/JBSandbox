package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;

public abstract class Expression extends SyntaxTree {

    private Type type;

    public Expression(Type type, List<Terminal> terminals, SourceRange location) {
        super(terminals, location);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
