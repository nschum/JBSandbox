package de.nschum.jbsandbox.ui;

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

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

/**
 * Parses a program on a background thread and notifies listeners of the result
 */
class BackgroundParser {

    private JBGrammar grammar = new JBGrammar();
    private Parser parser = new Parser(grammar);
    private List<Consumer<ParseResult>> listeners = new ArrayList<>();
    private int parseCount = 0;

    void parse(SourceFile sourceFile) {
        // main thread:
        final int currentParse = ++parseCount;

        CompletableFuture<ParseResult> future = CompletableFuture.supplyAsync(() -> {
            // async:
            try {
                List<ScannerToken> tokens = new JBScanner().scan(sourceFile);
                ParserTree parserTree = parser.parse(tokens);
                ASTBuilder astBuilder = new ASTBuilder(grammar);
                Program syntaxTree = astBuilder.createSyntaxTree(parserTree);

                return new ParseResult(sourceFile, syntaxTree,
                        astBuilder.getErrors().stream().map(EditorError::new).collect(toList()));
            } catch (UnexpectedTokenException e) {
                return new ParseResult(sourceFile, Arrays.asList(new EditorError(e)));
            } catch (IllegalTokenException e) {
                return new ParseResult(sourceFile, Arrays.asList(new EditorError(e)));
            } catch (MissingTokenException e) {
                return new ParseResult(sourceFile, Arrays.asList(new EditorError(e)));
            }
        });
        future.thenAcceptAsync(parseResult -> {
            // main thread:
            if (currentParse == parseCount) {
                // No new parse, no new edit, we can notify
                for (Consumer<ParseResult> listener : listeners) {
                    listener.accept(parseResult);
                }
            }
        }, SwingUtilities::invokeLater);
    }

    public void addResultListener(Consumer<ParseResult> consumer) {
        listeners.add(consumer);
    }
}
