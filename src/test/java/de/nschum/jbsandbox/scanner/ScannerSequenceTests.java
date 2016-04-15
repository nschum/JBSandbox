package de.nschum.jbsandbox.scanner;

import org.junit.Test;

import java.util.List;

import static de.nschum.jbsandbox.Matchers.contains;
import static de.nschum.jbsandbox.grammar.JBGrammar.*;
import static de.nschum.jbsandbox.scanner.ScannerTokenMatchers.sourceLocation;
import static de.nschum.jbsandbox.scanner.ScannerTokenMatchers.token;
import static org.junit.Assert.assertThat;

public class ScannerSequenceTests {

    Scanner scanner = new JBScanner();

    @Test
    public void shouldRecognizeMultipleTokens() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("( ) { } , -> + - * / ^ = var map reduce out print foo 123");

        // then
        assertThat(tokens, contains(
                token(PAREN_OPEN), token(PAREN_CLOSE), token(BRACE_OPEN), token(BRACE_CLOSE), token(COMMA),
                token(ARROW), token(PLUS), token(MINUS), token(STAR), token(SLASH), token(HAT), token(EQUALS),
                token(KEYWORD_VAR), token(KEYWORD_MAP), token(KEYWORD_REDUCE), token(KEYWORD_OUT), token(KEYWORD_PRINT),
                token(IDENTIFIER), token(NUMBER)
        ));
    }

    @Test
    public void shouldRecognizeTokensSeparatedByOperators() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("map(\"reduce\"}123");

        // then
        assertThat(tokens, contains(
                token(KEYWORD_MAP), token(PAREN_OPEN), token(STRING), token(BRACE_CLOSE), token(NUMBER)
        ));
    }

    @Test
    public void shouldRecognizeTokensSeparatedByLineBreaks() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("(\nfoo\n123");

        // then
        assertThat(tokens, contains(token(PAREN_OPEN), token(IDENTIFIER), token(NUMBER)));
    }

    @Test
    public void shouldNotRecognizeJoinedKeywords() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("printout");

        // then
        assertThat(tokens, contains(token(IDENTIFIER)));
    }

    @Test
    public void shouldIncludeSourceLocations() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("( )\nvar map\nfoo 123");

        // then
        assertThat(tokens, contains(
                sourceLocation(0, 0), sourceLocation(0, 2),
                sourceLocation(1, 0), sourceLocation(1, 4),
                sourceLocation(2, 0), sourceLocation(2, 4)
        ));
    }
}
