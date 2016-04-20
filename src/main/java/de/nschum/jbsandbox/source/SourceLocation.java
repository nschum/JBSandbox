package de.nschum.jbsandbox.source;

/**
 * Represents a position in the parsed source code
 */
public class SourceLocation {

    private int line;
    private int column;

    public SourceLocation(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        SourceLocation sourceLocation = (SourceLocation) o;

        if (line != sourceLocation.line) return false;
        return column == sourceLocation.column;

    }

    @Override
    public int hashCode() {
        return line << 16 ^ column;
    }

    @Override
    public String toString() {
        return line + ":" + column;
    }
}
