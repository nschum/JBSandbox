package de.nschum.jbsandbox.ui;

import de.nschum.jbsandbox.ast.ASTError;
import de.nschum.jbsandbox.parser.MissingTokenException;
import de.nschum.jbsandbox.parser.UnexpectedTokenException;
import de.nschum.jbsandbox.scanner.IllegalTokenException;
import de.nschum.jbsandbox.source.SourceRange;

/**
 * Scanning or parsing error created by BackgroundParser
 */
public class ParseError {

    private String message;
    private SourceRange location;

    public ParseError(String message, SourceRange location) {
        assert message != null;
        assert location != null;
        this.message = message;
        this.location = location;
    }

    public ParseError(ASTError astError) {
        this(astError.getMessage(), astError.getLocation());
    }

    public ParseError(UnexpectedTokenException e) {
        this(e.getMessage(), e.getLocation());
    }

    public ParseError(IllegalTokenException e) {
        this(e.getMessage(), new SourceRange(e.getLocation(), e.getLocation()));
    }

    public ParseError(MissingTokenException e) {
        this(e.getMessage(), e.getLocation());
    }

    public String getMessage() {
        return message;
    }

    public SourceRange getLocation() {
        return location;
    }
}
