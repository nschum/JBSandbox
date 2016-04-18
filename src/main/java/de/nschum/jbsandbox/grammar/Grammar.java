package de.nschum.jbsandbox.grammar;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static de.nschum.jbsandbox.grammar.GrammarToken.terminal;
import static java.util.stream.Collectors.toSet;

/**
 * A language grammar
 */
public interface Grammar {

    GrammarToken EPSILON = terminal("EPSILON"); // empty word
    GrammarToken EOF = terminal("EOF"); // end of input

    /**
     * Return all grammar rules
     */
    List<GrammarRule> getRules();

    /**
     * Return all tokens that appear in the grammar
     */
    default Set<GrammarToken> getTokens() {
        return Stream.concat(
                getRules().stream().flatMap(r -> r.getRightHandSide().stream()),
                getRules().stream().map(GrammarRule::getLeftHandSide))
                .collect(toSet());
    }

    /**
     * Return the grammar's start symbol
     */
    GrammarToken getStartSymbol();
}
