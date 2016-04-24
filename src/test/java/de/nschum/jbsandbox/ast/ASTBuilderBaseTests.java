package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.grammar.GrammarToken;
import de.nschum.jbsandbox.grammar.JBGrammar;
import de.nschum.jbsandbox.parser.MissingTokenException;
import de.nschum.jbsandbox.parser.Parser;
import de.nschum.jbsandbox.parser.ParserTree;
import de.nschum.jbsandbox.parser.UnexpectedTokenException;
import de.nschum.jbsandbox.scanner.IllegalTokenException;
import de.nschum.jbsandbox.scanner.JBScanner;
import de.nschum.jbsandbox.scanner.ScannerToken;
import de.nschum.jbsandbox.source.SourceFile;
import de.nschum.jbsandbox.source.SourceRange;
import org.hamcrest.Matcher;
import org.junit.Before;

import java.io.StringReader;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ASTBuilderBaseTests {

    private JBGrammar grammar = new JBGrammar();
    protected ASTBuilder astBuilder;

    @Before
    public void setUp() throws Exception {
        astBuilder = new ASTBuilder(grammar);
    }

    // matchers

    protected Matcher<Object> terminal(GrammarToken token, int start, int end) {
        return allOf(
                hasProperty("token", equalTo(token)),
                hasProperty("location", equalTo(new SourceRange(0, start, 0, end))));
    }

    // helpers

    protected Program parse(final String program)
            throws IllegalTokenException, UnexpectedTokenException, MissingTokenException {

        SourceFile file = new SourceFile("-", new StringReader(program));
        List<ScannerToken> tokens = new JBScanner().scan(file);
        ParserTree parseTree = new Parser(grammar).parse(tokens);

        return astBuilder.createSyntaxTree(parseTree);
    }

    protected Expression parseExpression(final String string)
            throws IllegalTokenException, UnexpectedTokenException, MissingTokenException {
        return ((OutStatement) parse("out " + string).getStatements().get(0)).getExpression();
    }

    protected void assertBuilderHasNoErrors() {
        assertThat(astBuilder.getErrors(), empty());
    }
}
