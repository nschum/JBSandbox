package de.nschum.jbsandbox.interpreter;

/**
 * An exception that occurs during program execution by the interpreter
 */
public class InterpreterRuntimeException extends RuntimeException {

    public InterpreterRuntimeException(String message) {
        super(message);
    }
}
