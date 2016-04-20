package de.nschum.jbsandbox.scanner;

import de.nschum.jbsandbox.source.SourceFile;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static de.nschum.jbsandbox.Matchers.contains;
import static de.nschum.jbsandbox.grammar.JBGrammar.STRING;
import static de.nschum.jbsandbox.scanner.ScannerTokenMatchers.token;
import static org.junit.Assert.assertThat;

public class ScannerStringTests {

    Scanner scanner = new JBScanner();

    private List<ScannerToken> scan(String input) throws IllegalTokenException {
        SourceFile file = new SourceFile("-", new StringReader(input));
        return scanner.scan(file);
    }

    @Test
    public void shouldRecognizeEmptyString() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("\"\"");

        // then
        assertThat(tokens, contains(token(STRING)));
    }

    @Test
    public void shouldRecognizeTextInString() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("\"x\"");

        // then
        assertThat(tokens, contains(token(STRING)));
    }

    @Test
    public void shouldRecognizeNumberInString() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("\"1\"");

        // then
        assertThat(tokens, contains(token(STRING)));
    }

    @Test
    public void shouldRecognizeKeywordInString() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("\"var\"");

        // then
        assertThat(tokens, contains(token(STRING)));
    }

    @Test
    public void shouldRecognizeSpaceInString() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("\" \"");

        // then
        assertThat(tokens, contains(token(STRING)));
    }
}
