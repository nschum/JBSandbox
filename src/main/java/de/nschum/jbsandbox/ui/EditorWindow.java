package de.nschum.jbsandbox.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class EditorWindow extends JFrame implements EditorWindowMenuBar.MenuHandler {

    private static final int DIVIDER_HEIGHT_MIN = 75;
    private static final int DIVIDER_HEIGHT_DEFAULT = 200;

    private JTextPane textPane;
    private JScrollPane editorScrollPane;
    private EditorWindowStatusBar statusBar;
    private JSplitPane logSplitPane;
    private JScrollPane logScrollPane;
    private JTextArea logTextArea;

    private Optional<File> file = Optional.empty();
    private EditorWindowMenuBar menu;
    private UndoManager undoManager = new UndoManager();

    private boolean logVisible = false;
    private int lastDividerHeight = DIVIDER_HEIGHT_DEFAULT;

    public static EditorWindow showNewEditorWindow(Optional<File> file) {
        EditorWindow editor = new EditorWindow(file);
        editor.setSize(800, 600);
        editor.setVisible(true);
        return editor;
    }

    private EditorWindow(Optional<File> file) {
        setModified(false);

        menu = new EditorWindowMenuBar(this);
        setJMenuBar(menu);

        textPane = new JTextPane();
        textPane.setCaretPosition(0);
        textPane.setMargin(new Insets(5, 5, 5, 5));
        textPane.addCaretListener(e -> menu.setCopyCutEnabled(textPane.getSelectedText() != null));

        file.ifPresent(this::readFile);
        textPane.getDocument().addUndoableEditListener(e -> {
            undoManager.addEdit(e.getEdit());
            updateUndo();
            setModified(true);
        });

        editorScrollPane = new JScrollPane(textPane);
        add(editorScrollPane);

        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        logTextArea.setBackground(Color.LIGHT_GRAY);
        logTextArea.setBorder(new EmptyBorder(0, 0, 0, 0));

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
        setTitle(file.map(File::getName).orElse("Untitled") + (modified ? " (modified)" : ""));
        getRootPane().putClientProperty("Window.documentModifieduu", modified);
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

    // MenuHandler

    @Override
    public void menuItemNewSelected(ActionEvent e) {
        showNewEditorWindow(Optional.empty());
    }

    @Override
    public void menuItemOpenSelected(ActionEvent e) {
        Optional<File> file = askForFileName("Open", FileDialog.LOAD);
        if (file.isPresent()) {
            showNewEditorWindow(file);
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
    }

    @Override
    public void menuItemRedoSelected(ActionEvent e) {
        undoManager.redo();
        updateUndo();
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
}
