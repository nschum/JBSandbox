package de.nschum.jbsandbox.scanner;

import org.junit.Test;

import java.util.List;

import static de.nschum.jbsandbox.Matchers.contains;
import static de.nschum.jbsandbox.grammar.GrammarToken.STRING;
import static de.nschum.jbsandbox.scanner.ScannerTokenMatchers.token;
import static org.junit.Assert.assertThat;

public class ScannerStringTests {

    Scanner scanner = new JBScanner();

    @Test
    public void shouldRecognizeEmptyString() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("\"\"");

        // then
        assertThat(tokens, contains(token(STRING)));
    }

    @Test
    public void shouldRecognizeTextInString() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("\"x\"");

        // then
        assertThat(tokens, contains(token(STRING)));
    }

    @Test
    public void shouldRecognizeNumberInString() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("\"1\"");

        // then
        assertThat(tokens, contains(token(STRING)));
    }

    @Test
    public void shouldRecognizeKeywordInString() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("\"var\"");

        // then
        assertThat(tokens, contains(token(STRING)));
    }

    @Test
    public void shouldRecognizeSpaceInString() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("\" \"");

        // then
        assertThat(tokens, contains(token(STRING)));
    }
}
