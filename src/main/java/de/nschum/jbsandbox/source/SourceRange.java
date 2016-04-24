package de.nschum.jbsandbox.source;

public class SourceRange {

    private SourceLocation start;
    private SourceLocation end;

    public SourceRange(int startLine, int startColumn, int endLine, int endColumn) {
        this(new SourceLocation(startLine, startColumn), new SourceLocation(endLine, endColumn));
    }

    public SourceRange(SourceLocation start, SourceLocation end) {
        assert start != null;
        assert end != null;
        this.start = start;
        this.end = end;
    }

    public SourceLocation getStart() {
        return start;
    }

    public SourceLocation getEnd() {
        return end;
    }

    public boolean isSingleLine() {
        return start.getLine() == end.getLine();
    }

    public boolean isSinglePoint() {
        return start.equals(end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SourceRange sourceRange = (SourceRange) o;

        return start.equals(sourceRange.start) && end.equals(sourceRange.end);
    }

    @Override
    public int hashCode() {
        return 31 * start.hashCode() + end.hashCode();
    }

    @Override
    public String toString() {
        return start + "-" + end;
    }

    /**
     * Pretty-print location and convert indexing to start with 1
     */
    public String toHumanReadableString() {
        if (isSinglePoint()) {
            return getStart().toHumanReadableString();
        } else {
            return getStart().toHumanReadableString() + "-" + getEnd().toHumanReadableString();
        }
    }
}
