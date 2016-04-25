package de.nschum.jbsandbox.interpreter;

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
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class InterpreterTests {

    private String run(String input)
            throws IllegalTokenException, UnexpectedTokenException, MissingTokenException, IOException,
            InterpreterRuntimeException {

        Program syntaxTree = parse(input);

        StringWriter output = new StringWriter();
        new Interpreter(new BufferedWriter(output)).execute(syntaxTree);
        return output.toString();
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

    // tests

    @Test
    public void shouldPrintString() throws Exception {
        String output = run("print \"hello world\"");

        assertThat(output, equalTo("hello world"));
    }

    @Test
    public void shouldConcatString() throws Exception {
        String output = run("print \"hello\" print \" world\"");

        assertThat(output, equalTo("hello world"));
    }

    @Test
    public void shouldPrintIntLiterals() throws Exception {
        String output = run("out 42");

        assertThat(output, equalTo("42"));
    }

    @Test
    public void shouldPrintFloatLiterals() throws Exception {
        String output = run("out 42.0");

        assertThat(output, equalTo("42.0"));
    }

    @Test
    public void shouldPrintRangeLiterals() throws Exception {
        String output = run("out {2, 4}");

        assertThat(output, equalTo("{2,4}"));
    }

    @Test
    public void shouldStoreVariableValuesAndRetrieveThem() throws Exception {
        String output = run("var foo = 42 out foo");

        assertThat(output, equalTo("42"));
    }

    @Test
    public void shouldAddInts() throws Exception {
        String output = run("var foo = 21 out foo + 21");

        assertThat(output, equalTo("42"));
    }

    @Test
    public void shouldSubtractInts() throws Exception {
        String output = run("var foo = 42 out foo - 21");

        assertThat(output, equalTo("21"));
    }

    @Test
    public void shouldMultiplyInts() throws Exception {
        String output = run("var foo = 21 out foo * 2");

        assertThat(output, equalTo("42"));
    }

    @Test
    public void shouldDivideInts() throws Exception {
        String output = run("var foo = 42 out foo / 2");

        assertThat(output, equalTo("21"));
    }

    @Test
    public void shouldApplyExponentToInts() throws Exception {
        String output = run("var foo = 2 out foo ^ 8");

        assertThat(output, equalTo("256"));
    }

    @Test
    public void shouldAddFloats() throws Exception {
        String output = run("var foo = 21.0 out foo + 21.0");

        assertThat(output, equalTo("42.0"));
    }

    @Test
    public void shouldSubtractFloats() throws Exception {
        String output = run("var foo = 42.0 out foo - 21.0");

        assertThat(output, equalTo("21.0"));
    }

    @Test
    public void shouldMultiplyFloats() throws Exception {
        String output = run("var foo = 21.0 out foo * 2.0");

        assertThat(output, equalTo("42.0"));
    }

    @Test
    public void shouldDivideFloats() throws Exception {
        String output = run("var foo = 42.0 out foo / 2.0");

        assertThat(output, equalTo("21.0"));
    }

    @Test
    public void shouldPromoteIntToFloat() throws Exception {
        String output = run("var foo = 21.0 out foo + 21.0");

        assertThat(output, equalTo("42.0"));
    }

    @Test
    public void shouldApplyExponentToFloats() throws Exception {
        String output = run("var foo = 2.0 out foo ^ 8.0");

        assertThat(output, equalTo("256.0"));
    }

    @Test
    public void shouldMapRanges() throws Exception {
        String output = run("out map({1, 5}, x -> 2 * x)");

        assertThat(output, equalTo("[2,4,6,8,10]"));
    }

    @Test
    public void shouldMapRangesToFloat() throws Exception {
        String output = run("out map({1, 5}, x -> x + 0.0)");

        assertThat(output, equalTo("[1.0,2.0,3.0,4.0,5.0]"));
    }

    @Test
    public void shouldMapFloatSequences() throws Exception {
        String output = run("out map(map({1, 5}, x -> x + 0.0), x -> x * 2)");

        assertThat(output, equalTo("[2.0,4.0,6.0,8.0,10.0]"));
    }

    @Test
    public void shouldMapRangesToSequenceSequences() throws Exception {
        String output = run("out map({1, 5}, i -> {1, i})");

        assertThat(output, equalTo("[{1,1},{1,2},{1,3},{1,4},{1,5}]"));
    }

    @Test
    public void shouldMapSequenceSequences() throws Exception {
        String output = run("out map(map({1, 5}, i -> {1, i}), i -> map(i, j -> 2 * j))");

        assertThat(output, equalTo("[[2],[2,4],[2,4,6],[2,4,6,8],[2,4,6,8,10]]"));
    }

    @Test
    public void shouldReduceRanges() throws Exception {
        String output = run("out reduce({1, 5}, 0, x y -> x + y)");

        assertThat(output, equalTo("15"));
    }

    @Test
    public void shouldReduceRangesToFloat() throws Exception {
        String output = run("out reduce({1, 5}, 0.0, x y -> x + y)");

        assertThat(output, equalTo("15.0"));
    }

    @Test
    public void shouldReduceFloatSequences() throws Exception {
        String output = run("out reduce(map({1, 5}, x -> x + 0.0), 0, x y -> x + y)");

        assertThat(output, equalTo("15.0"));
    }

    @Test
    public void shouldParseEntireProgram() throws Exception {
        String output = run("var n = 500\n" +
                "var sequence = map({0, n}, i -> (-1)^i / (2.0 * i + 1))\n" +
                "var pi = 4 * reduce(sequence, 0, x y -> x + y)\n" +
                "print \"pi = \"" +
                "out pi");

        assertThat(output, startsWith("pi = 3.14"));
    }

    @Test(expected = InterpreterRuntimeException.class)
    public void shouldNotAllowIntDivisionByZero() throws Exception {
        run("out 5 / 0");
    }

    @Test(expected = InterpreterRuntimeException.class)
    public void shouldNotAllowReverseRange() throws Exception {
        run("out {5, 0}");
    }

    @Test(expected = InterpreterCancelledException.class)
    public void shouldSupportCancellation() throws Exception {
        // given
        Program syntaxTree = parse("var n = 50000000 var sequence = map({0, n}, i -> (-1)^i)");
        StringWriter output = new StringWriter();
        Interpreter interpreter = new Interpreter(new BufferedWriter(output));

        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
            interpreter.cancel();
        });

        interpreter.execute(syntaxTree);
    }

}
