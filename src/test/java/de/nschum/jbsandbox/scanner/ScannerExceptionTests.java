package de.nschum.jbsandbox.scanner;

import de.nschum.jbsandbox.SourceLocation;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ScannerExceptionTests {

    Scanner scanner = new JBScanner();

    @Test(expected = IllegalTokenException.class)
    public void shouldThrowForExpressionStartingWithNumber() throws Exception {
        scanner.scan("1a");
    }

    @Test(expected = IllegalTokenException.class)
    public void shouldThrowForUnterminatedString() throws Exception {
        scanner.scan("\"");
    }

    @Test(expected = IllegalTokenException.class)
    public void shouldIncludeSourceLocationInException() throws Exception {
        try {
            scanner.scan("a 1a");
        } catch (IllegalTokenException e) {
            assertThat(e.getLocation(), equalTo(new SourceLocation(0, 2)));
            throw e;
        }
    }
}
