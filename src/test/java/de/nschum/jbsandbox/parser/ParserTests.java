package de.nschum.jbsandbox.parser;

import de.nschum.jbsandbox.grammar.Grammar;
import de.nschum.jbsandbox.grammar.GrammarRule;
import de.nschum.jbsandbox.grammar.GrammarToken;
import de.nschum.jbsandbox.scanner.ScannerToken;
import de.nschum.jbsandbox.source.SourceRange;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static de.nschum.jbsandbox.grammar.Grammar.EPSILON;
import static de.nschum.jbsandbox.parser.MockTokens.*;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ParserTests {

    // matchers

    private Matcher<ParserTree> parserTreeWithRule(GrammarRule rule) {
        return hasProperty("rule", equalTo(rule));
    }

    private Matcher<ParserTree> parserTreeWithToken(GrammarToken token, int startColumn, int endColumn) {
        if (token.isTerminal() && !token.equals(EPSILON)) {
            return allOf(
                    hasProperty("token", equalTo(token)),
                    hasProperty("location", equalTo(new SourceRange(0, startColumn, 0, endColumn))),
                    hasProperty("content", equalTo(Optional.of(token.toString()))));
        } else {
            return allOf(
                    hasProperty("token", equalTo(token)),
                    hasProperty("location", equalTo(new SourceRange(0, startColumn, 0, endColumn))));
        }
    }

    @SafeVarargs
    final private Matcher<ParserTree> parserTreeWithChildren(Matcher<ParserTree>... itemMatchers) {
        return hasProperty("children", contains(itemMatchers));
    }

    private Matcher<ParserTree> parserTreeWithChildren(Matcher<Collection<?>> itemsMatcher) {
        return hasProperty("children", itemsMatcher);
    }

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

    private List<ScannerToken> mockScannerTokens(GrammarToken... grammarTokens) {
        return IntStream.range(0, grammarTokens.length)
                .mapToObj(i -> {
                    final GrammarToken token = grammarTokens[i];
                    SourceRange location = new SourceRange(0, i * 10, 0, i * 10 + 1);
                    return new ScannerToken(token, token.toString(), location);
                })
                .collect(toList());
    }

    // tests

    @Test
    public void shouldParseRuleOfLiterals() throws Exception {
        // given
        GrammarRule rule = new GrammarRule(S, a, b);
        Grammar grammar = mockGrammarWithRules(rule);

        // when
        Parser parser = new Parser(grammar);

        // then
        final ParserTree parserTree = parser.parse(mockScannerTokens(a, b));
        assertThat(parserTree, parserTreeWithRule(rule));
        assertThat(parserTree, parserTreeWithChildren(
                parserTreeWithToken(a, 0, 1),
                parserTreeWithToken(b, 10, 11)
        ));
    }

    @Test
    public void shouldParseIndirectRuleOfLiterals() throws Exception {
        // given
        GrammarRule rule1 = new GrammarRule(S, a, B, c);
        GrammarRule rule2 = new GrammarRule(B, b);
        Grammar grammar = mockGrammarWithRules(rule1, rule2);

        // when
        Parser parser = new Parser(grammar);

        // then
        final ParserTree parserTree = parser.parse(mockScannerTokens(a, b, c));
        assertThat(parserTree, parserTreeWithRule(rule1));
        assertThat(parserTree, parserTreeWithChildren(
                parserTreeWithToken(a, 0, 1),
                allOf(
                        parserTreeWithToken(B, 10, 11),
                        parserTreeWithRule(rule2),
                        parserTreeWithChildren(parserTreeWithToken(b, 10, 11))),
                parserTreeWithToken(c, 20, 21)
        ));
    }

    @Test
    public void shouldParseRuleWithEpsilon() throws Exception {
        // given
        GrammarRule rule1 = new GrammarRule(S, a, B, c);
        GrammarRule rule2 = new GrammarRule(B, EPSILON);
        Grammar grammar = mockGrammarWithRules(rule1, rule2);

        // when
        Parser parser = new Parser(grammar);

        // then
        final ParserTree parserTree = parser.parse(mockScannerTokens(a, c));
        assertThat(parserTree, parserTreeWithRule(rule1));
        assertThat(parserTree, parserTreeWithChildren(
                parserTreeWithToken(a, 0, 1),
                allOf(
                        parserTreeWithToken(B, 1, 1),
                        parserTreeWithRule(rule2),
                        parserTreeWithChildren(parserTreeWithToken(EPSILON, 1, 1))),
                parserTreeWithToken(c, 10, 11)
        ));
    }

    @Test(expected = UnexpectedTokenException.class)
    public void shouldThrowsExceptionForUnexpectedToken() throws Exception {
        // given
        GrammarRule rule = new GrammarRule(S, a);
        Grammar grammar = mockGrammarWithRules(rule);

        // when
        Parser parser = new Parser(grammar);

        // then
        try {
            parser.parse(mockScannerTokens(b));
        } catch (UnexpectedTokenException e) {
            assertThat(e.getMessage(), equalTo("No rule for parsing b"));
            assertThat(e.getLocation(), equalTo(new SourceRange(0, 0, 0, 1)));
            throw e;
        }
    }

    @Test(expected = UnexpectedTokenException.class)
    public void shouldThrowsExceptionForTrailingTokens() throws Exception {
        // given
        GrammarRule rule = new GrammarRule(S, a);
        Grammar grammar = mockGrammarWithRules(rule);

        // when
        Parser parser = new Parser(grammar);

        // then
        try {
            parser.parse(mockScannerTokens(a, b));
        } catch (UnexpectedTokenException e) {
            assertThat(e.getMessage(), equalTo("No rule for parsing b"));
            assertThat(e.getLocation(), equalTo(new SourceRange(0, 10, 0, 11)));
            throw e;
        }
    }

    @Test(expected = MissingTokenException.class)
    public void shouldThrowsExceptionForMissingTerminal() throws Exception {
        // given
        GrammarRule rule = new GrammarRule(S, a, b);
        Grammar grammar = mockGrammarWithRules(rule);

        // when
        Parser parser = new Parser(grammar);

        // then
        try {
            parser.parse(mockScannerTokens(a));
        } catch (MissingTokenException e) {
            assertThat(e.getMessage(), equalTo("Expected token b"));
            throw e;
        }
    }

    @Test(expected = MissingTokenException.class)
    public void shouldThrowsExceptionForMissingSymbol() throws Exception {
        // given
        GrammarRule rule1 = new GrammarRule(S, a, B);
        GrammarRule rule2 = new GrammarRule(B, c);
        GrammarRule rule3 = new GrammarRule(B, b);
        Grammar grammar = mockGrammarWithRules(rule1, rule2, rule3);

        // when
        Parser parser = new Parser(grammar);

        // then
        try {
            parser.parse(mockScannerTokens(a));
        } catch (MissingTokenException e) {
            assertThat(e.getMessage(), equalTo("Expected token"));
            throw e;
        }
    }
}
