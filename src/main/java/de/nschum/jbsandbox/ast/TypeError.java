package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

/**
 * An error about incompatible types in the syntax tree
 */
public class TypeError extends ASTError {

    public TypeError(String message, SourceRange location) {
        super(message, location);
    }
}
