package de.nschum.jbsandbox.scanner;

import de.nschum.jbsandbox.source.SourceRange;
import de.nschum.jbsandbox.grammar.GrammarToken;

/**
 * Output created by the scanner
 */
public class ScannerToken {

    private GrammarToken grammarToken;
    private String content;
    private SourceRange location;

    public ScannerToken(GrammarToken grammarToken, String content, SourceRange location) {
        assert grammarToken != null;
        assert content != null;
        assert location != null;
        this.grammarToken = grammarToken;
        this.content = content;
        this.location = location;
    }

    /**
     * Returns the grammar token that was recognized
     */
    public GrammarToken getGrammarToken() {
        return grammarToken;
    }

    /**
     * Returns the input that was converted into this token
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the location of this token
     */
    public SourceRange getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "ScannerToken{" +
                "grammarToken=" + grammarToken +
                ", content='" + content + '\'' +
                '}';
    }
}
