package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

/**
 * The abstract syntax tree
 * <p>
 * While the ParserTree represents the grammar structure, this tree represents the semantic structure of a program.
 */
public abstract class SyntaxTree {

    private SourceRange location;

    protected SyntaxTree(SourceRange location) {
        this.location = location;
    }

    public SourceRange getLocation() {
        return location;
    }

    @Override
    public final String toString() {
        return toString("");
    }

    protected String toString(String indent) {
        return indent + getClass().getSimpleName();
    }
}
