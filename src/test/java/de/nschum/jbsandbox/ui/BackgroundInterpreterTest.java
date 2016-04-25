package de.nschum.jbsandbox.ui;

import de.nschum.jbsandbox.ast.ASTBuilder;
import de.nschum.jbsandbox.ast.Program;
import de.nschum.jbsandbox.grammar.JBGrammar;
import de.nschum.jbsandbox.parser.MissingTokenException;
import de.nschum.jbsandbox.parser.Parser;
import de.nschum.jbsandbox.parser.ParserTree;
import de.nschum.jbsandbox.parser.UnexpectedTokenException;
import de.nschum.jbsandbox.scanner.IllegalTokenException;
import de.nschum.jbsandbox.scanner.JBScanner;
import de.nschum.jbsandbox.scanner.ScannerToken;
import de.nschum.jbsandbox.source.SourceFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class BackgroundInterpreterTest {

    @Mock
    private SourceFile sourceFileMock;

    private CountDownLatch lock;

    @Before
    public void setUp() throws Exception {
        lock = new CountDownLatch(1);
    }

    private Program parse(String input) throws IllegalTokenException, UnexpectedTokenException, MissingTokenException {
        JBGrammar grammar = new JBGrammar();
        Parser parser = new Parser(grammar);

        SourceFile file = new SourceFile("-", new StringReader(input));
        List<ScannerToken> tokens = new JBScanner().scan(file);
        ParserTree parserTree = parser.parse(tokens);
        ASTBuilder astBuilder = new ASTBuilder(grammar);
        Program syntaxTree = astBuilder.createSyntaxTree(parserTree);

        assertThat(astBuilder.getErrors(), empty());
        return syntaxTree;
    }


    @Test
    public void shouldCallBackWithResults() throws Exception {
        // given
        BackgroundInterpreter interpreter = new BackgroundInterpreter();
        List<InterpreterResult> result = new ArrayList<>();
        interpreter.addResultListener(interpreterResult -> {
            result.add(interpreterResult);
            lock.countDown();
        });

        // when
        interpreter.run(sourceFileMock, parse("print \"hello\""));

        // then
        lock.await(2000, TimeUnit.MILLISECONDS);
        assertThat(result, contains(allOf(
                hasProperty("sourceFile", equalTo(sourceFileMock)),
                hasProperty("errors", empty()),
                hasProperty("output", equalTo("hello")))));
    }

    @Test
    public void shouldCallBackWithErrors() throws Exception {
        // given
        BackgroundInterpreter interpreter = new BackgroundInterpreter();
        List<InterpreterResult> result = new ArrayList<>();
        interpreter.addResultListener(interpreterResult -> {
            result.add(interpreterResult);
            lock.countDown();
        });

        // when
        interpreter.run(sourceFileMock, parse("out 1 / 0"));

        // then
        lock.await(2000, TimeUnit.MILLISECONDS);
        assertThat(result, contains(allOf(
                hasProperty("sourceFile", equalTo(sourceFileMock)),
                hasProperty("errors", contains(hasProperty("message", equalTo("Division by zero")))),
                hasProperty("output", equalTo("")))));
    }
}
