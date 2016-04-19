package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.SourceLocation;

/**
 * An error about incompatible types in the syntax tree
 */
public class TypeError extends ASTError {

    public TypeError(SourceLocation location) {
        super(location);
    }
}
