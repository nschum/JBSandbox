package de.nschum.jbsandbox.ui;

import de.nschum.jbsandbox.source.SourceFile;

import java.util.List;

/**
 * The execution output triggered by BackgroundParser
 */
public class InterpreterResult {

    private final SourceFile sourceFile;
    private final List<EditorError> errors;
    private final String output;

    InterpreterResult(SourceFile sourceFile, List<EditorError> errors, String output) {
        this.sourceFile = sourceFile;
        this.errors = errors;
        this.output = output;
    }

    public SourceFile getSourceFile() {
        return sourceFile;
    }

    public List<EditorError> getErrors() {
        return errors;
    }

    public String getOutput() {
        return output;
    }
}
