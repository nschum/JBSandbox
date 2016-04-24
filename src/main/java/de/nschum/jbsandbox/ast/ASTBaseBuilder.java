package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.grammar.GrammarToken;
import de.nschum.jbsandbox.grammar.JBGrammar;
import de.nschum.jbsandbox.parser.ParserTree;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Base class for ASTBuilder and its delegates
 */
public abstract class ASTBaseBuilder {

    private JBGrammar grammar;
    private List<ASTError> errors = new ArrayList<>();

    protected ASTBaseBuilder(JBGrammar grammar) {
        this.grammar = grammar;
    }

    protected JBGrammar getGrammar() {
        return grammar;
    }

    /**
     * Report an compile error for output
     */
    protected void reportError(ASTError error) {
        errors.add(error);
    }

    /**
     * Assert that the ParserTree matches the rule's right hand side.
     */
    protected void assertRule(ParserTree parserTree, GrammarToken... rightHandSide) {
        List<ParserTree> actual = parserTree.getChildren();
        assert actual.size() == rightHandSide.length;
        for (int i = 0; i < actual.size(); i++) {
            assert actual.get(i).getToken() == rightHandSide[i];
        }
    }

    public List<ASTError> getErrors() {
        return errors;
    }

    /**
     * Parse all terminals on the right hand side and create SyntaxTree nodes for them
     */
    protected List<Terminal> parseTerminals(ParserTree parserTree) {
        return parserTree.getChildren().stream()
                .filter(pt -> pt.getToken().isTerminal())
                .map(this::parseTerminal)
                .collect(toList());
    }

    Terminal parseTerminal(ParserTree parserTree) {
        return new Terminal(parserTree.getToken(), parserTree.getLocation());
    }
}
