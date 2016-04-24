package de.nschum.jbsandbox.ui;

import de.nschum.jbsandbox.source.SourceFile;
import de.nschum.jbsandbox.source.SourceRange;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * The main editor for a single file
 */
public class EditorWindow extends JFrame implements EditorWindowMenuBar.MenuHandler {

    private static final int DIVIDER_HEIGHT_MIN = 75;
    private static final int DIVIDER_HEIGHT_DEFAULT = 200;

    private JTextPane textPane;
    private JScrollPane editorScrollPane;
    private EditorWindowStatusBar statusBar;
    private JSplitPane logSplitPane;
    private JScrollPane logScrollPane;
    private LogTextArea logTextArea = new LogTextArea();

    private Delegate delegate;
    private Optional<File> file = Optional.empty();
    private Optional<ParseResult> parseResult = Optional.empty();
    private EditorWindowMenuBar menu;
    private UndoManager undoManager = new UndoManager();
    private ErrorHighlighter errorHighlighter;

    private boolean logVisible = false;
    private int lastDividerHeight = DIVIDER_HEIGHT_DEFAULT;
    private Optional<ParseError> selectedError;

    EditorWindow(Optional<File> file, Delegate delegate) {
        assert delegate != null;
        this.delegate = delegate;
        setModified(false);

        menu = new EditorWindowMenuBar(this);
        setJMenuBar(menu);

        textPane = new JTextPane() {
            @Override
            public String getToolTipText(MouseEvent event) {
                int position = viewToModel(event.getPoint());
                return getErrorForPosition(position).orElse(null);
            }
        };
        textPane.setCaretPosition(0);
        textPane.setMargin(new Insets(5, 5, 5, 5));
        textPane.addCaretListener(e -> {
            menu.setCopyCutEnabled(textPane.getSelectedText() != null);
            updateStatusBar();
        });
        ToolTipManager.sharedInstance().registerComponent(textPane);

        file.ifPresent(this::readFile);
        textPane.getDocument().addUndoableEditListener(e -> {
            undoManager.addEdit(e.getEdit());
            updateUndo();
            setModified(true);
            delegate.documentChanged();
            updateStatusBar();
        });
        errorHighlighter = new ErrorHighlighter(textPane.getHighlighter());

        editorScrollPane = new JScrollPane(textPane);
        add(editorScrollPane);

        logTextArea.addSelectionListener(this::selectError);

        logScrollPane = new JScrollPane(logTextArea);

        logSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        logSplitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        logSplitPane.setResizeWeight(1.0);
        logSplitPane.setDividerSize(3);
        logSplitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, e -> {
            lastDividerHeight = getHeight() - (Integer) e.getNewValue();
            if (lastDividerHeight < DIVIDER_HEIGHT_MIN) {
                lastDividerHeight = DIVIDER_HEIGHT_DEFAULT;
                setLogVisible(false);
            }
        });

        statusBar = new EditorWindowStatusBar();
        statusBar.addErrorActionListener(e -> setLogVisible(!logVisible));
        add(statusBar, BorderLayout.SOUTH);

        if (!"true".equals(System.getProperty("apple.laf.useScreenMenuBar"))) {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }

    private void updateUndo() {
        menu.setUndoTitle(undoManager.canUndo() ? Optional.of(undoManager.getUndoPresentationName()) : Optional.empty());
        menu.setRedoTitle(undoManager.canRedo() ? Optional.of(undoManager.getRedoPresentationName()) : Optional.empty());
    }

    private void readFile(File file) {
        try {
            textPane.setPage(file.toURI().toURL());
            this.file = Optional.of(file);
            setTitle(file.getName());
            getRootPane().putClientProperty("Window.documentFile", file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not read file");
        }
    }

    public void save() {
        if (!file.isPresent()) {
            file = askForFileName("Save", FileDialog.SAVE);
        }
        file.ifPresent(this::writeToFile);
    }

    private Optional<File> askForFileName(final String title, final int mode) {
        FileDialog fileDialog = new FileDialog(this, title, mode);
        fileDialog.setFilenameFilter((dir, name) -> name.endsWith(".jb"));
        fileDialog.setVisible(true);
        return Optional.ofNullable(fileDialog.getFile()).map(f -> new File(fileDialog.getDirectory(), f));
    }

    private void writeToFile(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(textPane.getText());
            setModified(false);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not save file");
        }
    }

    private void setModified(boolean modified) {
        setTitle(getFileName() + (modified ? " (modified)" : ""));
        getRootPane().putClientProperty("Window.documentModifieduu", modified);
    }

    String getFileName() {
        return file.map(File::getName).orElse("Untitled");
    }

    private void setLogVisible(boolean logVisible) {
        this.logVisible = logVisible;
        statusBar.setErrorSelected(logVisible);
        menu.setErrorsVisible(logVisible);

        if (logVisible) {
            remove(editorScrollPane);
            logSplitPane.setTopComponent(editorScrollPane);
            logSplitPane.setBottomComponent(logScrollPane);
            logSplitPane.setDividerLocation(getHeight() - lastDividerHeight);
            add(logSplitPane);
        } else {
            logSplitPane.remove(logSplitPane.getLeftComponent());
            logSplitPane.remove(logSplitPane.getRightComponent());
            remove(logSplitPane);
            add(editorScrollPane);
        }

        revalidate();
        textPane.requestFocusInWindow();
    }

    private void updateStatusBar() {
        int caretPosition = textPane.getCaretPosition();
        statusBar.setText(getErrorForPosition(caretPosition).orElse(""));
    }

    private Optional<String> getErrorForPosition(int position) {
        return parseResult.flatMap(pr -> {
            SourceFile sourceFile = pr.getSourceFile();
            return pr.getErrors().stream()
                    .filter(paresError -> {
                        SourceRange location = paresError.getLocation();
                        int start = sourceFile.offsetForLocation(location.getStart());
                        int end = sourceFile.offsetForLocation(location.getEnd());
                        return start <= position && position <= end;
                    })
                    .map(ParseError::getMessage)
                    .findFirst();

        });
    }

    public void setParseResult(Optional<ParseResult> parseResult) {
        this.parseResult = parseResult;
        selectedError = Optional.empty();
        menu.setErrorNavigationEnabled(parseResult.isPresent() && !parseResult.get().getErrors().isEmpty());

        parseResult.ifPresent(pr -> {
            List<ParseError> errors = pr.getErrors();
            logTextArea.setErrors(errors);
            statusBar.setErrorCount(errors.size());
            errorHighlighter.highlightErrors(pr.getSourceFile(), errors);
            updateStatusBar();
        });
    }

    String getText() {
        return textPane.getText();
    }

    private void selectError(ParseError error) {
        SourceFile sourceFile = parseResult.get().getSourceFile();
        textPane.setSelectionStart(sourceFile.offsetForLocation(error.getLocation().getStart()));
        textPane.setSelectionEnd(sourceFile.offsetForLocation(error.getLocation().getEnd()));
        selectedError = Optional.of(error);
    }

    // MenuHandler

    @Override
    public void menuItemNewSelected(ActionEvent e) {
        delegate.showNewWindow(Optional.empty());
    }

    @Override
    public void menuItemOpenSelected(ActionEvent e) {
        Optional<File> file = askForFileName("Open", FileDialog.LOAD);
        if (file.isPresent()) {
            delegate.showNewWindow(file);
        }
    }

    @Override
    public void menuItemCloseSelected(ActionEvent e) {
        dispose();
    }

    @Override
    public void menuItemSaveSelected(ActionEvent e) {
        save();
    }

    @Override
    public void menuItemUndoSelected(ActionEvent e) {
        undoManager.undo();
        updateUndo();
        delegate.documentChanged();
    }

    @Override
    public void menuItemRedoSelected(ActionEvent e) {
        undoManager.redo();
        updateUndo();
        delegate.documentChanged();
    }

    @Override
    public void menuItemCutSelected(ActionEvent e) {
        textPane.copy();
    }

    @Override
    public void menuItemCopySelected(ActionEvent e) {
        textPane.copy();
    }

    @Override
    public void menuItemPasteSelected(ActionEvent e) {
        textPane.paste();
    }

    @Override
    public void menuItemSelectAllSelected(ActionEvent e) {
        textPane.selectAll();
    }

    @Override
    public void menuItemErrorsSelected(ActionEvent e) {
        setLogVisible(!logVisible);
    }

    @Override
    public void menuItemNextErrorSelected(ActionEvent e) {
        List<ParseError> errors = parseResult.get().getErrors();
        int selectedIndex = selectedError.map(errors::indexOf).orElse(-1);
        selectError(errors.get(Math.floorMod(selectedIndex + 1, errors.size())));
    }

    @Override
    public void menuItemPreviousErrorSelected(ActionEvent e) {
        List<ParseError> errors = parseResult.get().getErrors();
        int selectedIndex = selectedError.map(errors::indexOf).orElse(errors.size());
        selectError(errors.get(Math.floorMod(selectedIndex - 1, errors.size())));
    }

    interface Delegate {
        default void documentChanged() {
        }
        void showNewWindow(Optional<File> file);
    }
}
