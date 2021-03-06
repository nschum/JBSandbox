package de.nschum.jbsandbox.scanner;

import de.nschum.jbsandbox.source.SourceFile;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static de.nschum.jbsandbox.Matchers.contains;
import static de.nschum.jbsandbox.grammar.JBGrammar.*;
import static de.nschum.jbsandbox.scanner.ScannerTokenMatchers.sourceRange;
import static de.nschum.jbsandbox.scanner.ScannerTokenMatchers.token;
import static org.junit.Assert.assertThat;

public class ScannerSequenceTests {

    Scanner scanner = new JBScanner();

    private List<ScannerToken> scan(String input) throws IllegalTokenException {
        SourceFile file = new SourceFile("-", new StringReader(input));
        return scanner.scan(file);
    }

    @Test
    public void shouldRecognizeMultipleTokens() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("( ) { } , -> + - * / ^ = var map reduce out print foo 123");

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
        final List<ScannerToken> tokens = scan("map(\"reduce\"}123");

        // then
        assertThat(tokens, contains(
                token(KEYWORD_MAP), token(PAREN_OPEN), token(STRING), token(BRACE_CLOSE), token(NUMBER)
        ));
    }

    @Test
    public void shouldRecognizeTokensSeparatedByLineBreaks() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("(\nfoo\n123");

        // then
        assertThat(tokens, contains(token(PAREN_OPEN), token(IDENTIFIER), token(NUMBER)));
    }

    @Test
    public void shouldNotRecognizeJoinedKeywords() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("printout");

        // then
        assertThat(tokens, contains(token(IDENTIFIER)));
    }

    @Test
    public void shouldIncludeSourceRanges() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("( )\nvar map\nfoo 123");

        // then
        assertThat(tokens, contains(
                sourceRange(0, 0, 0, 1), sourceRange(0, 2, 0, 3),
                sourceRange(1, 0, 1, 3), sourceRange(1, 4, 1, 7),
                sourceRange(2, 0, 2, 3), sourceRange(2, 4, 2, 7)
        ));
    }
}
