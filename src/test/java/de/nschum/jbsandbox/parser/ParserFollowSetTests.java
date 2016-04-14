package de.nschum.jbsandbox.parser;

import de.nschum.jbsandbox.grammar.Grammar;
import de.nschum.jbsandbox.grammar.GrammarRule;
import de.nschum.jbsandbox.grammar.GrammarToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static de.nschum.jbsandbox.grammar.GrammarToken.*;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ParserFollowSetTests {

    // mocks

    @Mock
    private ParserFirstSets firstSetsMock;

    private void mockFirstSet(final GrammarToken token, final GrammarToken... firstSet) {
        given(firstSetsMock.getFirstSet(token)).willReturn(Stream.of(firstSet).collect(toSet()));
    }

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
    public void followSetOfStartSymbolShouldBeEOF() {
        // given
        Grammar grammar = mockGrammarWithRules();

        // when
        ParserFollowSets parserFollowSets = new ParserFollowSets(grammar, null);

        // then
        assertThat(parserFollowSets.getFollowSet(PROGRAM), containsInAnyOrder(EOF));
    }

    @Test
    public void followSetOfSymbolShouldBeFirstSetOfFollowingSymbol() {
        // given
        mockFirstSet(OP, PLUS, MINUS);
        mockFirstSet(STMT, STAR, SLASH);
        GrammarRule rule1 = new GrammarRule(PROGRAM, EXPR, OP);
        GrammarRule rule2 = new GrammarRule(PROGRAM, EXPR, STMT);
        Grammar grammar = mockGrammarWithRules(rule1, rule2);

        // when
        ParserFollowSets parserFollowSets = new ParserFollowSets(grammar, firstSetsMock);

        // then
        assertThat(parserFollowSets.getFollowSet(EXPR), containsInAnyOrder(PLUS, MINUS, STAR, SLASH));
    }

    @Test
    public void followSetShouldInheritLeftHandSidesFollowSetIfLastSymbolInRule() {
        // given
        mockFirstSet(OP, PLUS, MINUS);
        mockFirstSet(STMT, STAR, SLASH);
        GrammarRule rule1 = new GrammarRule(PROGRAM, EXPR, OP);
        GrammarRule rule2 = new GrammarRule(PROGRAM, EXPR, STMT);
        GrammarRule rule3 = new GrammarRule(EXPR, EXPR_CONTINUED);
        Grammar grammar = mockGrammarWithRules(rule1, rule2, rule3);

        // when
        ParserFollowSets parserFollowSets = new ParserFollowSets(grammar, firstSetsMock);

        // then
        assertThat(parserFollowSets.getFollowSet(EXPR_CONTINUED), containsInAnyOrder(PLUS, MINUS, STAR, SLASH));
    }

    @Test
    public void followSetShouldInheritLeftHandSidesFollowSetIfOnlyEpsilonsFollow() {
        // given
        mockFirstSet(OP, PLUS, MINUS);
        mockFirstSet(STMT, STAR, SLASH);
        mockFirstSet(PROGRAM_CONTINUED, EPSILON);
        GrammarRule rule1 = new GrammarRule(PROGRAM, EXPR, OP);
        GrammarRule rule2 = new GrammarRule(PROGRAM, EXPR, STMT);
        GrammarRule rule3 = new GrammarRule(EXPR, EXPR_CONTINUED, PROGRAM_CONTINUED);
        Grammar grammar = mockGrammarWithRules(rule1, rule2, rule3);

        // when
        ParserFollowSets parserFollowSets = new ParserFollowSets(grammar, firstSetsMock);

        // then
        assertThat(parserFollowSets.getFollowSet(EXPR_CONTINUED), containsInAnyOrder(PLUS, MINUS, STAR, SLASH));
    }
}
