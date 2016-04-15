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

import static de.nschum.jbsandbox.grammar.Grammar.EOF;
import static de.nschum.jbsandbox.grammar.Grammar.EPSILON;
import static de.nschum.jbsandbox.parser.MockTokens.*;
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
                return S;
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
        assertThat(parserFollowSets.getFollowSet(S), containsInAnyOrder(EOF));
    }

    @Test
    public void followSetOfSymbolShouldBeFirstSetOfFollowingSymbol() {
        // given
        mockFirstSet(A, a, b);
        mockFirstSet(B, c, d);
        GrammarRule rule1 = new GrammarRule(C, D, A);
        GrammarRule rule2 = new GrammarRule(C, D, B);
        Grammar grammar = mockGrammarWithRules(rule1, rule2);

        // when
        ParserFollowSets parserFollowSets = new ParserFollowSets(grammar, firstSetsMock);

        // then
        assertThat(parserFollowSets.getFollowSet(D), containsInAnyOrder(a, b, c, d));
    }

    @Test
    public void followSetShouldInheritLeftHandSidesFollowSetIfLastSymbolInRule() {
        // given
        mockFirstSet(A, a, b);
        mockFirstSet(B, c, d);
        GrammarRule rule1 = new GrammarRule(C, D, A);
        GrammarRule rule2 = new GrammarRule(C, D, B);
        GrammarRule rule3 = new GrammarRule(D, F);
        Grammar grammar = mockGrammarWithRules(rule1, rule2, rule3);

        // when
        ParserFollowSets parserFollowSets = new ParserFollowSets(grammar, firstSetsMock);

        // then
        assertThat(parserFollowSets.getFollowSet(F), containsInAnyOrder(a, b, c, d));
    }

    @Test
    public void followSetShouldInheritLeftHandSidesFollowSetIfOnlyEpsilonsFollow() {
        // given
        mockFirstSet(A, a, b);
        mockFirstSet(B, c, d);
        mockFirstSet(C, EPSILON);
        GrammarRule rule1 = new GrammarRule(D, E, A);
        GrammarRule rule2 = new GrammarRule(D, E, B);
        GrammarRule rule3 = new GrammarRule(E, F, C);
        Grammar grammar = mockGrammarWithRules(rule1, rule2, rule3);

        // when
        ParserFollowSets parserFollowSets = new ParserFollowSets(grammar, firstSetsMock);

        // then
        assertThat(parserFollowSets.getFollowSet(F), containsInAnyOrder(a, b, c, d));
    }
}
