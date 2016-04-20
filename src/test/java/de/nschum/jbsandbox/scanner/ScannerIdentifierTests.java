package de.nschum.jbsandbox.scanner;

import de.nschum.jbsandbox.source.SourceFile;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static de.nschum.jbsandbox.Matchers.contains;
import static de.nschum.jbsandbox.grammar.JBGrammar.IDENTIFIER;
import static de.nschum.jbsandbox.scanner.ScannerTokenMatchers.token;
import static org.junit.Assert.assertThat;

public class ScannerIdentifierTests {

    Scanner scanner = new JBScanner();

    private List<ScannerToken> scan(String input) throws IllegalTokenException {
        SourceFile file = new SourceFile("-", new StringReader(input));
        return scanner.scan(file);
    }

    @Test
    public void shouldRecognizeAlphabeticString() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("a");

        // then
        assertThat(tokens, contains(token(IDENTIFIER)));
    }

    @Test
    public void shouldRecognizeAlphanumericString() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("a1b2");

        // then
        assertThat(tokens, contains(token(IDENTIFIER)));
    }
}
