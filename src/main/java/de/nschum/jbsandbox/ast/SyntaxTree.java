package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.List;
import java.util.function.Consumer;

/**
 * The abstract syntax tree
 * <p/>
 * While the ParserTree represents the grammar structure, this tree represents the semantic structure of a program.
 */
public abstract class SyntaxTree {

    private SourceRange location;
    /**
     * The terminal symbols (i.e. keywords, operators)
     */
    private List<Terminal> terminals;

    protected SyntaxTree(List<Terminal> terminals, SourceRange location) {
        this.location = location;
        this.terminals = terminals;
    }

    public SourceRange getLocation() {
        return location;
    }

    public List<Terminal> getTerminals() {
        return terminals;
    }

    /**
     * Visit tree recursively and call visitor with each node
     */
    public void visit(Consumer<SyntaxTree> visitor) {
        visitor.accept(this);
        terminals.stream().forEach(visitor::accept);
    }

    @Override
    public final String toString() {
        return toString("");
    }

    protected String toString(String indent) {
        return indent + getClass().getSimpleName();
    }
}
