package de.nschum.jbsandbox;

import de.nschum.jbsandbox.source.SourceFile;
import de.nschum.jbsandbox.source.SourceLocation;
import de.nschum.jbsandbox.source.SourceRange;

/**
 * Format and print command line errors
 */
public class ErrorPrinter {

    /**
     * Print an error with the location highlighted
     */
    public void print(String message, SourceFile file, SourceRange location) {
        System.err.println(message + ": "
                + file.getName() + ":"
                + location.toHumanReadableString());
        if (location.isSingleLine()) {
            if (location.isSinglePoint()) {
                printPointedAtSourceLine(file, location.getStart());
            } else {
                printUnderlinedSourceLine(file, location);
            }
        }
    }

    /**
     * Print an error with the location highlighted
     */
    public void print(String message, SourceFile file, SourceLocation location) {
        System.err.println(message + ": "
                + file.getName() + ":"
                + location.toHumanReadableString());
        printPointedAtSourceLine(file, location);
    }

    /**
     * Print an error with the location underlined by ~
     */
    private void printUnderlinedSourceLine(SourceFile file, SourceRange location) {
        try {
            String sourceLine = file.getLineForLocation(location.getStart());
            int start = location.getStart().getColumn();
            int end = location.getEnd().getColumn();
            String indent = repeat(" ", start);
            String highlight = repeat("~", end - start);

            selectColor("[36m");
            System.err.print(sourceLine.substring(0, start));
            selectColor("[31m");
            System.err.print(sourceLine.substring(start, end));
            selectColor("[36m");
            System.err.println(sourceLine.substring(end));
            selectColor("[31m");
            System.err.println(indent + highlight);
        } finally {
            selectColor("[0m");
        }
    }

    /**
     * Print an error with the location pointed at by ^
     */
    private void printPointedAtSourceLine(SourceFile file, SourceLocation location) {
        try {
            String sourceLine = file.getLineForLocation(location);
            String indent = repeat(" ", location.getColumn());
            String highlight = "^";

            selectColor("[36m");
            System.err.println(sourceLine);
            selectColor("[31m");
            System.err.println(indent + highlight);
        } finally {
            selectColor("[0m");
        }
    }

    private void selectColor(final String color) {
        System.err.print((char) 27 + color);
    }

    /**
     * Create a string with a character repeated
     */
    private String repeat(String s, int times) {
        return new String(new char[times]).replace("\0", s);
    }

    /**
     * Print a single line from a source file
     */
    private void printSourceLine(SourceFile file, SourceRange location) {
        System.err.println(file.getLineForLocation(location.getStart()));
    }
}
