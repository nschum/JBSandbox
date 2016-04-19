package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.grammar.JBGrammar;
import de.nschum.jbsandbox.parser.ParserTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Creates an abstract syntax tree that matches the language semantics from the grammar-based tree.
 */
public class ASTBuilder extends ASTBaseBuilder {

    public ASTBuilder(JBGrammar grammar) {
        super(grammar);
    }

    public Program createSyntaxTree(ParserTree parserTree) {
        List<Statement> statements = new ArrayList<>();
        ParserTree current = parserTree;
        Stack<Scope> scope = new Stack<>();
        scope.add(new Scope());

        JBGrammar grammar = getGrammar();
        while (current.getRule() == grammar.ruleFirstStatement || current.getRule() == grammar.ruleFurtherStatements) {

            ParserTree statementTree = current.getChild(0);
            ParserTree nextStatementTree = current.getChild(1);

            statements.add(parseStatement(statementTree, scope));
            current = nextStatementTree;
        }

        assert current.getRule() == grammar.ruleEof;

        return new Program(statements, parserTree.getLocation());
    }

    private Statement parseStatement(ParserTree parserTree, Stack<Scope> scopes) {
        final ASTStatementBuilder statementBuilder = new ASTStatementBuilder(getGrammar());
        final Statement statement = statementBuilder.parseStatement(parserTree, scopes);
        statementBuilder.getErrors().forEach(this::reportError);
        return statement;
    }
}
