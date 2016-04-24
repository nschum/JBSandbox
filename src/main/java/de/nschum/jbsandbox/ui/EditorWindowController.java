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
        });
        restartParse();
    }

    private void restartParse() {
        window.setParseResult(Optional.empty());
        backgroundParser.parse(new SourceFile(window.getFileName(), new StringReader(window.getText())));
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
