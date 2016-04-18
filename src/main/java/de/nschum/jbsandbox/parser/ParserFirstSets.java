package de.nschum.jbsandbox.parser;

import de.nschum.jbsandbox.grammar.Grammar;
import de.nschum.jbsandbox.grammar.GrammarRule;
import de.nschum.jbsandbox.grammar.GrammarToken;

import java.util.*;
import java.util.stream.Stream;

import static de.nschum.jbsandbox.grammar.Grammar.EPSILON;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * "First" sets for all grammar tokens
 * <p>
 * Calculates the set for all possible first terminal tokens for each token in a grammar.
 */
class ParserFirstSets {

    private Grammar grammar;
    private final HashMap<GrammarToken, List<GrammarRule>> rulesByLeftHandSide = new HashMap<>();

    public ParserFirstSets(Grammar grammar) {
        assert grammar != null;
        this.grammar = grammar;

        for (GrammarToken token : grammar.getTokens()) {
            final List<GrammarRule> rulesForToken =
                    grammar.getRules().stream()
                            .filter(rule -> rule.getLeftHandSide().equals(token))
                            .collect(toList());

            rulesByLeftHandSide.put(token, rulesForToken);
        }
    }

    public Set<GrammarToken> getFirstSet(GrammarRule rule) {
        return getFirstSet(rule.getRightHandSide().iterator());
    }

    private Set<GrammarToken> getFirstSet(Iterator<GrammarToken> tokenIterator) {
        final GrammarToken firstToken = tokenIterator.next();
        final Set<GrammarToken> firstSet = new HashSet<>(getFirstSet(firstToken));
        if (firstSet.contains(EPSILON) && tokenIterator.hasNext()) {
            firstSet.remove(EPSILON);
            firstSet.addAll(getFirstSet(tokenIterator));
        }
        return firstSet;
    }

    public Set<GrammarToken> getFirstSet(GrammarToken token) {
        if (token.isTerminal()) {
            return Stream.of(token).collect(toSet());
        } else {
            return rulesByLeftHandSide.get(token).stream()
                    .flatMap(rule -> getFirstSet(rule).stream())
                    .collect(toSet());
        }
    }
}
