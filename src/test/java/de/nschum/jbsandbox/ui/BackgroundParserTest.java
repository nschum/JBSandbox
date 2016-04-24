package de.nschum.jbsandbox.ui;

import de.nschum.jbsandbox.source.SourceFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class BackgroundParserTest {

    private CountDownLatch lock;

    @Before
    public void setUp() throws Exception {
        lock = new CountDownLatch(1);
    }

    private SourceFile mockSourceFile(String input) {
        return new SourceFile("-", new StringReader(input));
    }

    @Test
    public void shouldCallBackWithResults() throws Exception {
        // given
        BackgroundParser Parser = new BackgroundParser();
        List<ParseResult> result = new ArrayList<>();
        Parser.addResultListener(ParserResult -> {
            result.add(ParserResult);
            lock.countDown();
        });
        SourceFile sourceFile = mockSourceFile("print \"hello\"");

        // when
        Parser.parse(sourceFile);

        // then
        lock.await(2000, TimeUnit.MILLISECONDS);
        assertThat(result, contains(allOf(
                hasProperty("sourceFile", equalTo(sourceFile)),
                hasProperty("errors", empty()),
                hasProperty("syntaxTree", hasProperty("present", equalTo(true))))));
    }

    @Test
    public void shouldCallBackWithSyntaxErrors() throws Exception {
        // given
        BackgroundParser Parser = new BackgroundParser();
        List<ParseResult> result = new ArrayList<>();
        Parser.addResultListener(ParserResult -> {
            result.add(ParserResult);
            lock.countDown();
        });
        SourceFile sourceFile = mockSourceFile("out {1, 2.0}");

        // when
        Parser.parse(sourceFile);

        // then
        lock.await(2000, TimeUnit.MILLISECONDS);
        assertThat(result, contains(allOf(
                hasProperty("sourceFile", equalTo(sourceFile)),
                hasProperty("errors", contains(
                        hasProperty("message", equalTo("Range bounds must be integers (was {INT,FLOAT})")))),
                hasProperty("syntaxTree", hasProperty("present", equalTo(true))))));
    }

    @Test
    public void shouldCallBackWithUnexpectedTokenException() throws Exception {
        // given
        BackgroundParser Parser = new BackgroundParser();
        List<ParseResult> result = new ArrayList<>();
        Parser.addResultListener(ParserResult -> {
            result.add(ParserResult);
            lock.countDown();
        });
        SourceFile sourceFile = mockSourceFile("out out");

        // when
        Parser.parse(sourceFile);

        // then
        lock.await(2000, TimeUnit.MILLISECONDS);
        assertThat(result, contains(hasProperty("errors", contains(
                hasProperty("message", equalTo("No rule for parsing KEYWORD_OUT"))))));
    }

    @Test
    public void shouldCallBackWithIllegalTokenException() throws Exception {
        // given
        BackgroundParser Parser = new BackgroundParser();
        List<ParseResult> result = new ArrayList<>();
        Parser.addResultListener(ParserResult -> {
            result.add(ParserResult);
            lock.countDown();
        });
        SourceFile sourceFile = mockSourceFile("out 1e");

        // when
        Parser.parse(sourceFile);

        // then
        lock.await(2000, TimeUnit.MILLISECONDS);
        assertThat(result, contains(hasProperty("errors", contains(
                hasProperty("message", equalTo("Unrecognized token"))))));
    }

    @Test
    public void shouldCallBackWithMissingTokenException() throws Exception {
        // given
        BackgroundParser Parser = new BackgroundParser();
        List<ParseResult> result = new ArrayList<>();
        Parser.addResultListener(ParserResult -> {
            result.add(ParserResult);
            lock.countDown();
        });
        SourceFile sourceFile = mockSourceFile("out");

        // when
        Parser.parse(sourceFile);

        // then
        lock.await(2000, TimeUnit.MILLISECONDS);
        assertThat(result, contains(hasProperty("errors", contains(
                hasProperty("message", equalTo("Expected token"))))));
    }
}
