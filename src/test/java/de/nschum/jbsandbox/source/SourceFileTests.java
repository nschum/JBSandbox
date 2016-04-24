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

    @Test
    public void shouldReturnIndexAfterLastLine() {
        SourceFile sourceFile = new SourceFile("-", new StringReader("foo\n"));

        assertThat(sourceFile.offsetForLocation(new SourceLocation(1, 0)), equalTo(4));
    }

    @Test
    public void shouldReturnOffsetAsColumnInFirstLine() {
        SourceFile sourceFile = new SourceFile("-", new StringReader("foo"));

        assertThat(sourceFile.locationForOffset(2), equalTo(new SourceLocation(0, 2)));
    }

    @Test
    public void shouldReturnOffsetMinusPreviousLines() {
        SourceFile sourceFile = new SourceFile("-", new StringReader("foo\nbar\nbaz"));

        assertThat(sourceFile.locationForOffset(9), equalTo(new SourceLocation(2, 1)));
    }

    @Test
    public void shouldReturnPreceedingLineOnNewline() {
        SourceFile sourceFile = new SourceFile("-", new StringReader("foo\nbar"));

        assertThat(sourceFile.locationForOffset(3), equalTo(new SourceLocation(0, 3)));
    }

    @Test
    public void shouldReturnFollowingLineOnCharacterAfterNewline() {
        SourceFile sourceFile = new SourceFile("-", new StringReader("foo\nbar"));

        assertThat(sourceFile.locationForOffset(4), equalTo(new SourceLocation(1, 0)));
    }

    @Test
    public void shouldConvertIndexAfterLastLine() {
        SourceFile sourceFile = new SourceFile("-", new StringReader("foo\n"));

        assertThat(sourceFile.locationForOffset(4), equalTo(new SourceLocation(1, 0)));
    }
}
