package de.nschum.jbsandbox.interpreter;

import de.nschum.jbsandbox.SourceRange;

/**
 * An exception that occurs during program execution by the interpreter
 */
public class InterpreterRuntimeException extends RuntimeException {

    private SourceRange location;

    public InterpreterRuntimeException(String message, SourceRange location) {
        super(message);
        this.location = location;
    }

    public SourceRange getLocation() {
        return location;
    }
}
