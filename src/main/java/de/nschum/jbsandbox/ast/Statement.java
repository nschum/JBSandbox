package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

public abstract class Statement extends SyntaxTree {
    public Statement(SourceRange location) {
        super(location);
    }
}
