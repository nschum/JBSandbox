package de.nschum.jbsandbox.parser;

import de.nschum.jbsandbox.grammar.Grammar;
import de.nschum.jbsandbox.grammar.GrammarRule;
import de.nschum.jbsandbox.grammar.GrammarToken;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static de.nschum.jbsandbox.grammar.GrammarToken.*;
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
                return PROGRAM;
            }
        };
    }

    // tests

    @Test
    public void shouldIncludeDirectTokenInFirstSet() {
        // given
        GrammarRule rule = new GrammarRule(OP, PLUS, MINUS);
        Grammar grammar = mockGrammarWithRules(rule);

        // when
        ParserFirstSets parserFirstSets = new ParserFirstSets(grammar);

        // then
        assertThat(parserFirstSets.getFirstSet(rule), containsInAnyOrder(PLUS));
        assertThat(parserFirstSets.getFirstSet(OP), containsInAnyOrder(PLUS));
    }

    @Test
    public void shouldIncludeIndirectTokensInFirstSet() {
        // given
        GrammarRule rule1 = new GrammarRule(STMT, OP);
        GrammarRule rule2 = new GrammarRule(OP, PLUS, STAR);
        GrammarRule rule3 = new GrammarRule(OP, MINUS, SLASH);
        Grammar grammar = mockGrammarWithRules(rule1, rule2, rule3);

        // when
        ParserFirstSets parserFirstSets = new ParserFirstSets(grammar);

        // then
        assertThat(parserFirstSets.getFirstSet(rule1), containsInAnyOrder(PLUS, MINUS));
        assertThat(parserFirstSets.getFirstSet(STMT), containsInAnyOrder(PLUS, MINUS));
    }

    @Test
    public void shouldSkipEpsilonInFirstSet() {
        // given
        GrammarRule rule1 = new GrammarRule(STMT, EXPR, OP);
        GrammarRule rule2 = new GrammarRule(EXPR, EPSILON);
        GrammarRule rule3 = new GrammarRule(OP, PLUS, STAR);
        GrammarRule rule4 = new GrammarRule(OP, MINUS, SLASH);
        Grammar grammar = mockGrammarWithRules(rule1, rule2, rule3, rule4);

        // when
        ParserFirstSets parserFirstSets = new ParserFirstSets(grammar);

        // then
        assertThat(parserFirstSets.getFirstSet(rule1), containsInAnyOrder(PLUS, MINUS));
        assertThat(parserFirstSets.getFirstSet(STMT), containsInAnyOrder(PLUS, MINUS));
    }

    @Test
    public void shouldIncludeEpsilonIfNoOtherTokens() {
        // given
        GrammarRule rule1 = new GrammarRule(STMT, EXPR, OP);
        GrammarRule rule2 = new GrammarRule(EXPR, EPSILON);
        GrammarRule rule3 = new GrammarRule(OP, EPSILON);
        Grammar grammar = mockGrammarWithRules(rule1, rule2, rule3);

        // when
        ParserFirstSets parserFirstSets = new ParserFirstSets(grammar);

        // then
        assertThat(parserFirstSets.getFirstSet(rule1), containsInAnyOrder(EPSILON));
        assertThat(parserFirstSets.getFirstSet(STMT), containsInAnyOrder(EPSILON));
    }
}
