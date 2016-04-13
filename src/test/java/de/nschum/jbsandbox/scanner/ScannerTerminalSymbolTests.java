package de.nschum.jbsandbox.scanner;

import org.junit.Test;

import java.util.List;

import static de.nschum.jbsandbox.Matchers.contains;
import static de.nschum.jbsandbox.grammar.GrammarToken.*;
import static de.nschum.jbsandbox.scanner.ScannerTokenMatchers.token;
import static org.junit.Assert.assertThat;

public class ScannerTerminalSymbolTests {

    Scanner scanner = new JBScanner();

    @Test
    public void shouldRecognizeParenOpen() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("(");

        // then
        assertThat(tokens, contains(token(PAREN_OPEN)));
    }

    @Test
    public void shouldRecognizeParenClose() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan(")");

        // then
        assertThat(tokens, contains(token(PAREN_CLOSE)));
    }

    @Test
    public void shouldRecognizeBraceOpen() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("{");

        // then
        assertThat(tokens, contains(token(BRACE_OPEN)));
    }

    @Test
    public void shouldRecognizeBraceClose() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("}");

        // then
        assertThat(tokens, contains(token(BRACE_CLOSE)));
    }

    @Test
    public void shouldRecognizeComma() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan(",");

        // then
        assertThat(tokens, contains(token(COMMA)));
    }

    @Test
    public void shouldRecognizeArrow() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("->");

        // then
        assertThat(tokens, contains(token(ARROW)));
    }

    @Test
    public void shouldRecognizePlus() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("+");

        // then
        assertThat(tokens, contains(token(PLUS)));
    }

    @Test
    public void shouldRecognizeMinus() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("-");

        // then
        assertThat(tokens, contains(token(MINUS)));
    }

    @Test
    public void shouldRecognizeStar() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("*");

        // then
        assertThat(tokens, contains(token(STAR)));
    }

    @Test
    public void shouldRecognizeSlash() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("/");

        // then
        assertThat(tokens, contains(token(SLASH)));
    }

    @Test
    public void shouldRecognizeHat() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("^");

        // then
        assertThat(tokens, contains(token(HAT)));
    }

    @Test
    public void shouldRecognizeEquals() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("=");

        // then
        assertThat(tokens, contains(token(EQUALS)));
    }

    @Test
    public void shouldRecognizeVar() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("var");

        // then
        assertThat(tokens, contains(token(KEYWORD_VAR)));
    }

    @Test
    public void shouldRecognizeMap() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("map");

        // then
        assertThat(tokens, contains(token(KEYWORD_MAP)));
    }

    @Test
    public void shouldRecognizeReduce() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("reduce");

        // then
        assertThat(tokens, contains(token(KEYWORD_REDUCE)));
    }

    @Test
    public void shouldRecognizeOut() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("out");

        // then
        assertThat(tokens, contains(token(KEYWORD_OUT)));
    }

    @Test
    public void shouldRecognizePrint() throws Exception {
        // when
        final List<ScannerToken> tokens = scanner.scan("print");

        // then
        assertThat(tokens, contains(token(KEYWORD_PRINT)));
    }
}
