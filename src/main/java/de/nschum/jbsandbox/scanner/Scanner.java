package de.nschum.jbsandbox.scanner;

import de.nschum.jbsandbox.SourceLocation;
import de.nschum.jbsandbox.SourceRange;
import de.nschum.jbsandbox.grammar.GrammarToken;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isWhitespace;
import static java.util.stream.Collectors.joining;

/**
 * Scanner to tokenize input
 * <p>
 * The scanner uses several regular expressions that are combined into a large pattern.
 * Each match group in the large pattern represents one token kind.
 */
public class Scanner {

    protected static final String NON_WORD_LOOKAHEAD = "(?!\\w)";

    private List<String> patterns = new ArrayList<>();
    private List<GrammarToken> tokens = new ArrayList<>();
    private Pattern combinedPattern;

    // setup

    protected void registerOperator(String operator, GrammarToken token) {
        patterns.add(Pattern.quote(operator));
        tokens.add(token);
    }

    protected void registerKeyword(final String keyword, final GrammarToken token) {
        patterns.add(Pattern.quote(keyword) + NON_WORD_LOOKAHEAD);
        tokens.add(token);
    }

    protected void registerPattern(String pattern, GrammarToken token) {
        patterns.add(pattern);
        tokens.add(token);
    }

    protected void compile() {
        combinedPattern = Pattern.compile("(?:(" + patterns.stream().collect(joining(")|(")) + ")).*");
    }

    // scanning

    public List<ScannerToken> scan(String input) throws IllegalTokenException {
        return scan(new StringReader(input));
    }

    public List<ScannerToken> scan(Reader reader) throws IllegalTokenException {
        if (combinedPattern == null) {
            throw new IllegalStateException("Regular expressions were not compiled");
        }

        java.util.Scanner scanner = new java.util.Scanner(reader);
        try {
            List<ScannerToken> tokens = new ArrayList<>();
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                scanLine(scanner.nextLine(), lineNumber, tokens);
                lineNumber++;
            }
            return tokens;
        } finally {
            scanner.close();
        }
    }

    private void scanLine(String line, int lineNumber, List<ScannerToken> output) throws IllegalTokenException {

        int column = 0;
        while (column < line.length()) {

            if (isWhitespace(line.charAt(column))) {
                column++;
                continue;
            }

            SourceLocation location = new SourceLocation(lineNumber, column);

            final Matcher matcher = combinedPattern.matcher(line);

            if (!matcher.find(column) || matcher.start() != column) {
                throw new IllegalTokenException(location);
            }

            // Which sub-pattern caused the match? That's the token we are looking for.
            assert matcher.groupCount() == tokens.size();
            int group = findNonEmptyMatchGroup(matcher);

            int length = matcher.end(group) - matcher.start(group);
            SourceRange range = new SourceRange(location, new SourceLocation(lineNumber, column + length));

            output.add(new ScannerToken(tokens.get(group - 1), matcher.group(group), range));
            column = matcher.end(group);
        }
    }

    private int findNonEmptyMatchGroup(Matcher matcher) {
        for (int i = 1; i <= matcher.groupCount(); i++) {
            if (matcher.start(i) != -1) {
                return i;
            }
        }
        throw new IllegalArgumentException("No non-empty match group");
    }
}
