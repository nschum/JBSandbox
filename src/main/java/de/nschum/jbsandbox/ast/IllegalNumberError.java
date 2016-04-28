package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

/**
 * An error about a number that's too small or big
 */
public class IllegalNumberError extends ASTError {

    public IllegalNumberError(String message, SourceRange location) {
        super(message, location);
    }
}
