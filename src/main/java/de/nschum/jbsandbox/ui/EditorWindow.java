package de.nschum.jbsandbox.ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class EditorWindow extends JFrame {

    private JTextPane textPane;
    private Optional<File> file = Optional.empty();

    public static EditorWindow showNewEditorWindow(Optional<File> file) {
        EditorWindow editor = new EditorWindow(file);
        editor.setSize(800, 600);
        editor.setVisible(true);
        return editor;
    }

    private EditorWindow(Optional<File> file) {
        setTitle("Untitled");

        textPane = new JTextPane();
        textPane.setCaretPosition(0);
        textPane.setMargin(new Insets(5, 5, 5, 5));
        file.ifPresent(this::readFile);

        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane);

        if (!"true".equals(System.getProperty("apple.laf.useScreenMenuBar"))) {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }

    private void readFile(File file) {
        try {
            textPane.setPage(file.toURI().toURL());
            this.file = Optional.of(file);
            setTitle(file.getName());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not read file");
        }
    }
}
