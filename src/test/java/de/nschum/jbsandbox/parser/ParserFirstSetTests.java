package de.nschum.jbsandbox.parser;

import de.nschum.jbsandbox.grammar.Grammar;
import de.nschum.jbsandbox.grammar.GrammarRule;
import de.nschum.jbsandbox.grammar.GrammarToken;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static de.nschum.jbsandbox.grammar.Grammar.EPSILON;
import static de.nschum.jbsandbox.parser.MockTokens.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class ParserFirstSetTests {

    // mocks

    private Grammar mockGrammarWithRules(GrammarRule... rules) {
        return new Grammar() {
            @Override
            public List<GrammarRule> getRules() {
                return Arrays.asList(rules);
            }

            @Override
            public GrammarToken getStartSymbol() {
                return S;
            }
        };
    }

    // tests

    @Test
    public void shouldIncludeDirectTokenInFirstSet() {
        // given
        GrammarRule rule = new GrammarRule(A, a, b);
        Grammar grammar = mockGrammarWithRules(rule);

        // when
        ParserFirstSets parserFirstSets = new ParserFirstSets(grammar);

        // then
        assertThat(parserFirstSets.getFirstSet(rule), containsInAnyOrder(a));
        assertThat(parserFirstSets.getFirstSet(A), containsInAnyOrder(a));
    }

    @Test
    public void shouldIncludeIndirectTokensInFirstSet() {
        // given
        GrammarRule rule1 = new GrammarRule(A, B);
        GrammarRule rule2 = new GrammarRule(B, a, b);
        GrammarRule rule3 = new GrammarRule(B, c, d);
        Grammar grammar = mockGrammarWithRules(rule1, rule2, rule3);

        // when
        ParserFirstSets parserFirstSets = new ParserFirstSets(grammar);

        // then
        assertThat(parserFirstSets.getFirstSet(rule1), containsInAnyOrder(a, c));
        assertThat(parserFirstSets.getFirstSet(A), containsInAnyOrder(a, c));
    }

    @Test
    public void shouldSkipEpsilonInFirstSet() {
        // given
        GrammarRule rule1 = new GrammarRule(A, B, C);
        GrammarRule rule2 = new GrammarRule(B, EPSILON);
        GrammarRule rule3 = new GrammarRule(C, a, b);
        GrammarRule rule4 = new GrammarRule(C, c, d);
        Grammar grammar = mockGrammarWithRules(rule1, rule2, rule3, rule4);

        // when
        ParserFirstSets parserFirstSets = new ParserFirstSets(grammar);

        // then
        assertThat(parserFirstSets.getFirstSet(rule1), containsInAnyOrder(a, c));
        assertThat(parserFirstSets.getFirstSet(A), containsInAnyOrder(a, c));
    }

    @Test
    public void shouldIncludeEpsilonIfNoOtherTokens() {
        // given
        GrammarRule rule1 = new GrammarRule(A, B, C);
        GrammarRule rule2 = new GrammarRule(B, EPSILON);
        GrammarRule rule3 = new GrammarRule(C, EPSILON);
        Grammar grammar = mockGrammarWithRules(rule1, rule2, rule3);

        // when
        ParserFirstSets parserFirstSets = new ParserFirstSets(grammar);

        // then
        assertThat(parserFirstSets.getFirstSet(rule1), containsInAnyOrder(EPSILON));
        assertThat(parserFirstSets.getFirstSet(A), containsInAnyOrder(EPSILON));
    }
}
