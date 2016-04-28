package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.grammar.GrammarToken;
import de.nschum.jbsandbox.source.SourceRange;

import java.util.Collections;
import java.util.function.Consumer;

/**
 * Represents keywords, operators, parentheses or braces.
 * <p/>
 * Terminals have no further semantics, but are useful to have in the syntax tree for tool support
 * (e.g. syntax highlighting).
 */
public class Terminal extends SyntaxTree {

    private final GrammarToken token;

    public Terminal(GrammarToken token, SourceRange location) {
        super(Collections.emptyList(), location);
        this.token = token;
    }

    public GrammarToken getToken() {
        return token;
    }

    public void visit(Consumer<SyntaxTree> visitor) {
        visitor.accept(this);
    }

    @Override
    protected String toString(String indent) {
        return indent + token;
    }
}
