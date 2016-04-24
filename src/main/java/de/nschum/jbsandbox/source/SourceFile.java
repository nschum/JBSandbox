package de.nschum.jbsandbox.source;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Represents a source file and its contents
 */
public class SourceFile {

    private String name;
    private List<String> lines;

    public SourceFile(String name, Reader reader) {
        assert name != null;
        assert reader != null;

        this.name = name;
        lines = new BufferedReader(reader).lines().collect(toList());
    }

    public String getName() {
        return name;
    }

    public List<String> getLines() {
        return lines;
    }

    public String getLineForLocation(SourceLocation location) {
        return lines.get(location.getLine());
    }

    /**
     * Return the character offset for a SourceLocation
     */
    public int offsetForLocation(SourceLocation location) {
        int offset = 0;
        for (int i = 0; i < location.getLine(); i++) {
            offset += getLineLength(i);
        }
        return offset + location.getColumn();
    }

    /**
     * Returns a source location for a character offset
     */
    public SourceLocation locationForOffset(int offset) {
        int line = 0;
        int remaining = offset;
        while (line < lines.size() && remaining >= getLineLength(line)) {
            remaining -= getLineLength(line);
            line++;
        }
        return new SourceLocation(line, remaining);
    }

    private int getLineLength(int i) {
        return lines.get(i).length() + "\n".length();
    }
}
