package de.nschum.jbsandbox.source;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a source file and its contents
 */
public class SourceFile {

    private String name;
    private List<String> lines = new ArrayList<>();

    public SourceFile(String name, Reader reader) {
        assert name != null;
        assert reader != null;

        this.name = name;
        java.util.Scanner scanner = new java.util.Scanner(reader);
        try {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } finally {
            scanner.close();

        }
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
}
