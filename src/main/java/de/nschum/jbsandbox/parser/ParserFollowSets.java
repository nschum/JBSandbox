package de.nschum.jbsandbox.parser;

import de.nschum.jbsandbox.grammar.Grammar;
import de.nschum.jbsandbox.grammar.GrammarRule;
import de.nschum.jbsandbox.grammar.GrammarToken;

import java.util.*;
import java.util.stream.Stream;

import static de.nschum.jbsandbox.grammar.Grammar.EOF;
import static de.nschum.jbsandbox.grammar.Grammar.EPSILON;
import static java.util.stream.Collectors.toSet;

/**
 * "Follow" sets for all grammar tokens
 * <p>
 * Calculates the set for all possible terminal tokens following each token in a grammar.
 */
class ParserFollowSets {

    private final Map<GrammarToken, Set<GrammarToken>> followSets = new HashMap<>();

    public ParserFollowSets(Grammar grammar, ParserFirstSets firstSets) {
        assert grammar != null;
        assert firstSets != null;

        for (GrammarToken token : grammar.getTokens()) {
            followSets.put(token, new HashSet<>());
        }

        // start symbol is always followed by EOF
        followSets.put(grammar.getStartSymbol(), Stream.of(EOF).collect(toSet()));

        boolean changed;
        do {
            changed = false;
            for (GrammarRule rule : grammar.getRules()) {
                final List<GrammarToken> rhs = rule.getRightHandSide();
                for (int i = 0; i < rhs.size() - 1; i++) {
                    final GrammarToken token = rhs.get(i);
                    final GrammarToken followingToken = rhs.get(i + 1);

                    Set<GrammarToken> followingFirstSet = new HashSet<>(firstSets.getFirstSet(followingToken));
                    if (followingFirstSet.contains(EPSILON)) {
                        changed |= addToFollowSet(token, followSets.get(rule.getLeftHandSide()));
                        followingFirstSet.remove(EPSILON);
                    }
                    changed |= addToFollowSet(token, followingFirstSet);
                }
                final GrammarToken token = rhs.get(rhs.size() - 1);
                changed |= addToFollowSet(token, followSets.get(rule.getLeftHandSide()));
            }
        } while (changed);
    }

    private boolean addToFollowSet(GrammarToken token, Set<GrammarToken> tokens) {
        return followSets.get(token).addAll(tokens);
    }

    public Set<GrammarToken> getFollowSet(GrammarToken token) {
        return followSets.get(token);
    }
}
