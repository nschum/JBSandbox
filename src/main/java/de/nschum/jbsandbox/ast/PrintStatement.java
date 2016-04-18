package de.nschum.jbsandbox.ast;

public class PrintStatement extends Statement {

    private String string;

    public PrintStatement(String string) {
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
