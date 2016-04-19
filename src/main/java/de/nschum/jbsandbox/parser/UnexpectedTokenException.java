package de.nschum.jbsandbox.parser;

import de.nschum.jbsandbox.SourceRange;
import de.nschum.jbsandbox.scanner.ScannerToken;

/**
 * Exception when parser would have required a token but the input had a different token
 */
public class UnexpectedTokenException extends Exception {

    private final SourceRange location;

    public UnexpectedTokenException(ScannerToken token) {
        super("No rule for parsing " + token.getGrammarToken());
        location = token.getLocation();
    }

    public SourceRange getLocation() {
        return location;
    }
}
