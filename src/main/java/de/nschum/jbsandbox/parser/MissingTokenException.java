package de.nschum.jbsandbox.parser;

import de.nschum.jbsandbox.grammar.GrammarToken;

/**
 * Exception when parser would have required a token but the input was empty
 */
public class MissingTokenException extends Exception {

    public MissingTokenException(GrammarToken token) {
        super(token.isTerminal() ? "Expected token " + token : "Expected token");
    }
}
