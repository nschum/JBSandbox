package de.nschum.jbsandbox.scanner;

import de.nschum.jbsandbox.source.SourceFile;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static de.nschum.jbsandbox.Matchers.contains;
import static de.nschum.jbsandbox.grammar.JBGrammar.*;
import static de.nschum.jbsandbox.scanner.ScannerTokenMatchers.token;
import static org.junit.Assert.assertThat;

public class ScannerTerminalSymbolTests {

    Scanner scanner = new JBScanner();

    private List<ScannerToken> scan(String input) throws IllegalTokenException {
        SourceFile file = new SourceFile("-", new StringReader(input));
        return scanner.scan(file);
    }

    @Test
    public void shouldRecognizeParenOpen() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("(");

        // then
        assertThat(tokens, contains(token(PAREN_OPEN)));
    }

    @Test
    public void shouldRecognizeParenClose() throws Exception {
        // when
        final List<ScannerToken> tokens = scan(")");

        // then
        assertThat(tokens, contains(token(PAREN_CLOSE)));
    }

    @Test
    public void shouldRecognizeBraceOpen() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("{");

        // then
        assertThat(tokens, contains(token(BRACE_OPEN)));
    }

    @Test
    public void shouldRecognizeBraceClose() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("}");

        // then
        assertThat(tokens, contains(token(BRACE_CLOSE)));
    }

    @Test
    public void shouldRecognizeComma() throws Exception {
        // when
        final List<ScannerToken> tokens = scan(",");

        // then
        assertThat(tokens, contains(token(COMMA)));
    }

    @Test
    public void shouldRecognizeArrow() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("->");

        // then
        assertThat(tokens, contains(token(ARROW)));
    }

    @Test
    public void shouldRecognizePlus() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("+");

        // then
        assertThat(tokens, contains(token(PLUS)));
    }

    @Test
    public void shouldRecognizeMinus() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("-");

        // then
        assertThat(tokens, contains(token(MINUS)));
    }

    @Test
    public void shouldRecognizeStar() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("*");

        // then
        assertThat(tokens, contains(token(STAR)));
    }

    @Test
    public void shouldRecognizeSlash() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("/");

        // then
        assertThat(tokens, contains(token(SLASH)));
    }

    @Test
    public void shouldRecognizeHat() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("^");

        // then
        assertThat(tokens, contains(token(HAT)));
    }

    @Test
    public void shouldRecognizeEquals() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("=");

        // then
        assertThat(tokens, contains(token(EQUALS)));
    }

    @Test
    public void shouldRecognizeVar() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("var");

        // then
        assertThat(tokens, contains(token(KEYWORD_VAR)));
    }

    @Test
    public void shouldRecognizeMap() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("map");

        // then
        assertThat(tokens, contains(token(KEYWORD_MAP)));
    }

    @Test
    public void shouldRecognizeReduce() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("reduce");

        // then
        assertThat(tokens, contains(token(KEYWORD_REDUCE)));
    }

    @Test
    public void shouldRecognizeOut() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("out");

        // then
        assertThat(tokens, contains(token(KEYWORD_OUT)));
    }

    @Test
    public void shouldRecognizePrint() throws Exception {
        // when
        final List<ScannerToken> tokens = scan("print");

        // then
        assertThat(tokens, contains(token(KEYWORD_PRINT)));
    }
}
