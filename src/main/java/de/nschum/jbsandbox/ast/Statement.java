package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;

public abstract class Statement extends SyntaxTree {
    public Statement(List<Terminal> terminals, SourceRange location) {
        super(terminals, location);
    }
}
