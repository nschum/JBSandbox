package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.SourceLocation;

/**
 * An error about a reference to an unknown variable in the syntax tree
 */
public class UnresolvedVariableError extends ASTError {

    public UnresolvedVariableError(SourceLocation location) {
        super(location);
    }
}
