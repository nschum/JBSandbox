package de.nschum.jbsandbox.ast;

/**
 * The abstract syntax tree
 * <p>
 * While the ParserTree represents the grammar structure, this tree represents the semantic structure of a program.
 */
public abstract class SyntaxTree {

    @Override
    public final String toString() {
        return toString("");
    }

    protected String toString(String indent) {
        return indent + getClass().getSimpleName();
    }
}
