package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.joining;

/**
 * A collection of statements that make a full program
 */
public class Program extends SyntaxTree {

    private final List<Statement> statements;

    public Program(List<Statement> statements, SourceRange location) {
        super(Arrays.asList(), location);
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return this.statements;
    }

    @Override
    protected String toString(String indent) {
        return super.toString(indent) + "\n"
                + statements.stream().map(c -> c.toString(indent + "  ")).collect(joining("\n"));
    }

    @Override
    public void visit(Consumer<SyntaxTree> visitor) {
        visitor.accept(this);
        for (Statement statement : statements) {
            statement.visit(visitor);
        }
    }
}
