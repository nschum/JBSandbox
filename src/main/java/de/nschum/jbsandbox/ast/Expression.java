package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;

/**
 * Base class for all expressions, i.e. side-effect-free code that returns a value
 */
public abstract class Expression extends SyntaxTree {

    private final Type type;

    public Expression(Type type, List<Terminal> terminals, SourceRange location) {
        super(terminals, location);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
