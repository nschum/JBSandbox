package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;

/**
 * Base class for all statements, i.e. code that has side effects
 */
public abstract class Statement extends SyntaxTree {
    public Statement(List<Terminal> terminals, SourceRange location) {
        super(terminals, location);
    }
}
