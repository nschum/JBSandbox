package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;

/**
 * Statement that outputs a static string
 */
public class PrintStatement extends Statement {

    private final String string;

    public PrintStatement(String string, List<Terminal> terminals, SourceRange location) {
        super(terminals, location);
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
