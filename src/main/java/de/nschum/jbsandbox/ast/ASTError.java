package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

/**
 * An error created during semantic analysis when creating the AST from the ParserTree
 */
public abstract class ASTError {

    private String message;
    private SourceRange location;

    public ASTError(String message, SourceRange location) {
        assert message != null;
        assert location != null;
        this.message = message;
        this.location = location;
    }

    public String getMessage() {
        return message;
    }

    public SourceRange getLocation() {
        return location;
    }
}
