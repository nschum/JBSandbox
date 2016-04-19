package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.SourceRange;

public abstract class Statement extends SyntaxTree {
    public Statement(SourceRange location) {
        super(location);
    }
}
