package de.nschum.jbsandbox.source;

import org.junit.Test;

import java.io.StringReader;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SourceFileTests {

    @Test
    public void shouldSeparateLinesByNewline() {
        SourceFile sourceFile = new SourceFile("-", new StringReader("foo\nbar\nbaz"));

        assertThat(sourceFile.getLines(), contains("foo", "bar", "baz"));
    }

    @Test
    public void shouldReturnColumnAsOffsetInFirstLine() {
        SourceFile sourceFile = new SourceFile("-", new StringReader("foo"));

        assertThat(sourceFile.offsetForLocation(new SourceLocation(0, 2)), equalTo(2));
    }

    @Test
    public void shouldReturnColumnAndFirstTwoLinesAsOffsetInThirdLine() {
        SourceFile sourceFile = new SourceFile("-", new StringReader("foo\nbar\nbaz"));

        assertThat(sourceFile.offsetForLocation(new SourceLocation(2, 1)), equalTo(9));
    }
}
