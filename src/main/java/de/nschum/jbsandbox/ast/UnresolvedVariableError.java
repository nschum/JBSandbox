package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

/**
 * An error about a reference to an unknown variable in the syntax tree
 */
public class UnresolvedVariableError extends ASTError {

    public UnresolvedVariableError(String message, SourceRange location) {
        super(message, location);
    }
}
