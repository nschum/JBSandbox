package de.nschum.jbsandbox.ui;

import de.nschum.jbsandbox.ast.ASTError;
import de.nschum.jbsandbox.parser.MissingTokenException;
import de.nschum.jbsandbox.parser.UnexpectedTokenException;
import de.nschum.jbsandbox.scanner.IllegalTokenException;
import de.nschum.jbsandbox.source.SourceRange;

/**
 * Scanning or parsing error created by BackgroundParser
 */
public class EditorError {

    private String message;
    private SourceRange location;

    public EditorError(String message, SourceRange location) {
        assert message != null;
        assert location != null;
        this.message = message;
        this.location = location;
    }

    public EditorError(ASTError astError) {
        this(astError.getMessage(), astError.getLocation());
    }

    public EditorError(UnexpectedTokenException e) {
        this(e.getMessage(), e.getLocation());
    }

    public EditorError(IllegalTokenException e) {
        this(e.getMessage(), new SourceRange(e.getLocation(), e.getLocation()));
    }

    public EditorError(MissingTokenException e) {
        this(e.getMessage(), e.getLocation());
    }

    public String getMessage() {
        return message;
    }

    public SourceRange getLocation() {
        return location;
    }
}
