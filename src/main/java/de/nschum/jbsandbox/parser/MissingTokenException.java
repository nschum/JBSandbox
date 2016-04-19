package de.nschum.jbsandbox.parser;

import de.nschum.jbsandbox.SourceRange;
import de.nschum.jbsandbox.grammar.GrammarToken;

/**
 * Exception when parser would have required a token but the input was empty
 */
public class MissingTokenException extends Exception {

    private final SourceRange location;

    public MissingTokenException(GrammarToken token, SourceRange location) {
        super(token.isTerminal() ? "Expected token " + token : "Expected token");
        this.location = location;
    }

    public SourceRange getLocation() {
        return location;
    }
}
