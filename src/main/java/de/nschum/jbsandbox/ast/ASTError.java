package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.SourceLocation;

/**
 * An error created during semantic analysis when creating the AST from the ParserTree
 */
public abstract class ASTError {

    private SourceLocation location;

    public ASTError(SourceLocation location) {
        assert location != null;
        this.location = location;
    }

    public SourceLocation getLocation() {
        return location;
    }
}
