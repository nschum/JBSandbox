package de.nschum.jbsandbox.scanner;

import de.nschum.jbsandbox.SourceLocation;
import de.nschum.jbsandbox.SourceRange;
import de.nschum.jbsandbox.grammar.GrammarToken;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;

public class ScannerTokenMatchers {

    public static Matcher<? extends ScannerToken> token(GrammarToken token) {
        return hasProperty("grammarToken", equalTo(token));
    }

    public static Matcher<? extends ScannerToken> sourceLocation(int line, int column) {
        return hasProperty("location", equalTo(new SourceLocation(line, column)));
    }

    public static Matcher<? extends ScannerToken> sourceRange(int startLine, int startColumn,
                                                              int endLine, int endColumn) {
        return hasProperty("location", equalTo(new SourceRange(startLine, startColumn, endLine, endColumn)));
    }
}
