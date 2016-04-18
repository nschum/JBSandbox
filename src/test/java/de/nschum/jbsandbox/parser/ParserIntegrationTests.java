package de.nschum.jbsandbox.parser;

import de.nschum.jbsandbox.grammar.JBGrammar;
import de.nschum.jbsandbox.scanner.JBScanner;
import de.nschum.jbsandbox.scanner.ScannerToken;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ParserIntegrationTests {

    @Test
    public void shouldParseEntireProgram() throws Exception {
        final Parser parser = new Parser(new JBGrammar());

        final String input = "var n = 5\n" +
                "        var sequence = map({1, n}, i -> (-1)^i / (2 * i + 1))\n" +
                "        var pi = 4 * reduce(sequence, 0, x y -> x + y)\n" +
                "        print \"pi = \"" +
                "        out pi";
        final List<ScannerToken> tokens = new JBScanner().scan(input);
        final ParserTree parseTree = parser.parse(tokens);

        assertThat(parseTree.getToken(), equalTo(JBGrammar.PROGRAM));
    }
}
