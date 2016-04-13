package de.nschum.jbsandbox.scanner;

import de.nschum.jbsandbox.SourceLocation;

public class IllegalTokenException extends Exception {

    private final SourceLocation location;

    public IllegalTokenException(SourceLocation location) {
        super("Unrecognized token");
        assert location != null;
        this.location = location;
    }

    public SourceLocation getLocation() {
        return location;
    }
}
