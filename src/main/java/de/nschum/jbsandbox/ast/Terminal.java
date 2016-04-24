package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.grammar.GrammarToken;
import de.nschum.jbsandbox.source.SourceRange;

import java.util.Collections;
import java.util.function.Consumer;

public class Terminal extends SyntaxTree {

    private GrammarToken token;

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
