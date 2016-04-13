package de.nschum.jbsandbox.scanner;

import org.junit.Test;

import java.util.List;

import static de.nschum.jbsandbox.Matchers.contains;
import static de.nschum.jbsandbox.grammar.GrammarToken.NUMBER;
import static de.nschum.jbsandbox.scanner.ScannerTokenMatchers.token;
import static org.junit.Assert.assertThat;

public class ScannerNumberTests {

    Scanner scanner = new JBScanner();

    @Test
    public void shouldRecognizeInt() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("0");

        // then
        assertThat(tokens, contains(token(NUMBER)));
    }

    @Test
    public void shouldRecognizeLong() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("4000000000000000000");

        // then
        assertThat(tokens, contains(token(NUMBER)));
    }

    @Test
    public void shouldRecognizeDouble() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("1.0000000000000002");

        // then
        assertThat(tokens, contains(token(NUMBER)));
    }
}
