package de.nschum.jbsandbox.parser;

import de.nschum.jbsandbox.grammar.Grammar;
import de.nschum.jbsandbox.grammar.GrammarRule;
import de.nschum.jbsandbox.grammar.GrammarToken;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static de.nschum.jbsandbox.grammar.Grammar.EPSILON;

/**
 * Rule lookup table for LL(1) parser
 */
class ParserTable {

    private Grammar grammar;
    private ParserFirstSets firstSets;
    private ParserFollowSets followSets;
    private Map<TableEntry, GrammarRule> table = new HashMap<>();

    ParserTable(Grammar grammar, ParserFirstSets firstSets, ParserFollowSets followSets) {
        this.grammar = grammar;
        this.firstSets = firstSets;
        this.followSets = followSets;

        for (GrammarRule rule : grammar.getRules()) {
            final Set<GrammarToken> firstSet = firstSets.getFirstSet(rule);
            for (GrammarToken token : grammar.getTokens()) {
                if (token.isTerminal()) {
                    final GrammarToken lhs = rule.getLeftHandSide();
                    if (firstSet.contains(token)
                            || firstSet.contains(EPSILON) && followSets.getFollowSet(lhs).contains(token)) {
                        addToTable(token, rule);
                    }
                }
            }
        }
    }

    private void addToTable(GrammarToken token, GrammarRule rule) {
        final TableEntry key = new TableEntry(token, rule.getLeftHandSide());
        if (table.get(key) != null) {
            throw new IllegalArgumentException("Grammar is not LL(1)");
        }
        table.put(key, rule);
    }

    Optional<GrammarRule> getRuleForTerminal(GrammarToken terminal, GrammarToken nonTerminal) {
        final TableEntry key = new TableEntry(terminal, nonTerminal);
        return Optional.ofNullable(table.get(key));
    }

    private class TableEntry {
        private GrammarToken terminal;
        private GrammarToken nonTerminal;

        public TableEntry(GrammarToken terminal, GrammarToken nonTerminal) {
            assert terminal != null;
            assert nonTerminal != null;
            this.terminal = terminal;
            this.nonTerminal = nonTerminal;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TableEntry tableEntry = (TableEntry) o;

            return terminal.equals(tableEntry.terminal) && nonTerminal.equals(tableEntry.nonTerminal);
        }

        @Override
        public int hashCode() {
            return 31 * terminal.hashCode() + nonTerminal.hashCode();
        }

        @Override
        public String toString() {
            return "TableEntry{" + terminal + ", " + nonTerminal + '}';
        }
    }
}
