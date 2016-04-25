package de.nschum.jbsandbox.ui;

import de.nschum.jbsandbox.source.SourceFile;

import java.io.File;
import java.io.StringReader;
import java.util.Optional;

/**
 * Controller for EditorWindow
 */
public class EditorWindowController implements EditorWindow.Delegate {

    private EditorWindow window;

    private BackgroundParser backgroundParser = new BackgroundParser();
    private BackgroundInterpreter backgroundInterpreter = new BackgroundInterpreter();

    public static EditorWindowController showNewEditorWindow(Optional<File> file) {
        EditorWindowController editor = new EditorWindowController(file);
        editor.window.setSize(800, 600);
        editor.window.setVisible(true);
        return editor;
    }

    private EditorWindowController(Optional<File> file) {
        window = new EditorWindow(file, this);

        backgroundParser.addResultListener(parseResult -> {
            window.setParseResult(Optional.of(parseResult));
            if (parseResult.getErrors().isEmpty()) {
                startExecution(parseResult);
            }
        });
        backgroundInterpreter.addResultListener(interpreterResult -> {
            window.setInterpreterResult(Optional.of(interpreterResult));
        });
        restartParse();
    }

    private void restartParse() {
        window.setParseResult(Optional.empty());
        window.setInterpreterResult(Optional.empty());
        backgroundInterpreter.cancel();
        backgroundParser.parse(new SourceFile(window.getFileName(), new StringReader(window.getText())));
    }

    private void startExecution(ParseResult parseResult) {
        window.setInterpreterResult(Optional.empty());
        backgroundInterpreter.run(parseResult.getSourceFile(), parseResult.getSyntaxTree().get());
    }

    // EditorWindow.Delegate

    @Override
    public void documentChanged() {
        restartParse();
    }

    @Override
    public void showNewWindow(Optional<File> file) {
        showNewEditorWindow(file);
    }
}
