package de.nschum.jbsandbox.ui;

import de.nschum.jbsandbox.ast.SyntaxTree;
import de.nschum.jbsandbox.source.SourceFile;

import java.util.List;
import java.util.Optional;

/**
 * The parsing output created by BackgroundParser
 */
public class ParseResult {

    private final SourceFile sourceFile;
    private final List<ParseError> errors;
    private final Optional<SyntaxTree> syntaxTree;

    ParseResult(SourceFile sourceFile, SyntaxTree syntaxTree, List<ParseError> errors) {
        this(sourceFile, errors, Optional.of(syntaxTree));
    }

    ParseResult(SourceFile sourceFile, List<ParseError> errors) {
        this(sourceFile, errors, Optional.empty());
    }

    private ParseResult(SourceFile sourceFile, List<ParseError> errors, Optional<SyntaxTree> syntaxTree) {
        this.sourceFile = sourceFile;
        this.errors = errors;
        this.syntaxTree = syntaxTree;
    }

    public SourceFile getSourceFile() {
        return sourceFile;
    }

    public List<ParseError> getErrors() {
        return errors;
    }

    public Optional<SyntaxTree> getSyntaxTree() {
        return syntaxTree;
    }
}
