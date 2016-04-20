package de.nschum.jbsandbox.scanner;

import de.nschum.jbsandbox.source.SourceFile;
import de.nschum.jbsandbox.source.SourceLocation;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ScannerExceptionTests {

    Scanner scanner = new JBScanner();

    private List<ScannerToken> scan(String input) throws IllegalTokenException {
        SourceFile file = new SourceFile("-", new StringReader(input));
        return scanner.scan(file);
    }

    @Test(expected = IllegalTokenException.class)
    public void shouldThrowForExpressionStartingWithNumber() throws Exception {
        scan("1a");
    }

    @Test(expected = IllegalTokenException.class)
    public void shouldThrowForUnterminatedString() throws Exception {
        scan("\"");
    }

    @Test(expected = IllegalTokenException.class)
    public void shouldIncludeSourceLocationInException() throws Exception {
        try {
            scan("a 1a");
        } catch (IllegalTokenException e) {
            assertThat(e.getLocation(), equalTo(new SourceLocation(0, 2)));
            throw e;
        }
    }
}
