package de.nschum.jbsandbox.ast;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class Program extends SyntaxTree {

    private List<Statement> statements;

    public Program(List<Statement> statements) {
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
}
