package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.SourceRange;

/**
 * An error created during semantic analysis when creating the AST from the ParserTree
 */
public abstract class ASTError {

    private SourceRange location;

    public ASTError(SourceRange location) {
        assert location != null;
        this.location = location;
    }

    public SourceRange getLocation() {
        return location;
    }
}
