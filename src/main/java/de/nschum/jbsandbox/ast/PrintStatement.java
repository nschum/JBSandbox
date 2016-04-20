package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

public class PrintStatement extends Statement {

    private String string;

    public PrintStatement(String string, SourceRange location) {
        super(location);
        this.string = string;
    }

    public String getString() {
        return string;
    }

    @Override
    protected String toString(String indent) {
        return indent + "print \"" + string + "\"";
    }
}
