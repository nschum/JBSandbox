package de.nschum.jbsandbox.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Optional;

import static java.awt.event.InputEvent.SHIFT_DOWN_MASK;

/**
 * Menu for main editor window
 */
class EditorWindowMenuBar extends JMenuBar {

    private static int ACCELERATOR = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    private MenuHandler menuHandler;
    private JMenuItem undo;
    private JMenuItem redo;
    private JMenuItem cut;
    private JMenuItem copy;
    private JCheckBoxMenuItem errors;
    private JMenuItem nextError;
    private JMenuItem previousError;

    EditorWindowMenuBar(MenuHandler menuHandler) {
        assert menuHandler != null;

        this.menuHandler = menuHandler;

        add(createFileMenu());
        add(createEditMenu());
        add(createViewMenu());

        setCopyCutEnabled(false);
        setUndoTitle(Optional.empty());
        setRedoTitle(Optional.empty());
        setErrorNavigationEnabled(false);
    }

    private JMenuItem createMenuItem(String text, ActionListener actionListener, int key) {
        return createMenuItem(text, actionListener, key, 0);
    }

    private JMenuItem createMenuItem(String text, ActionListener actionListener, int key, int modifier) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(actionListener);
        item.setAccelerator(KeyStroke.getKeyStroke(key, ACCELERATOR | modifier));
        return item;
    }

    private JMenu createFileMenu() {
        JMenu menu = new JMenu("File");
        menu.add(createMenuItem("New", menuHandler::menuItemNewSelected, KeyEvent.VK_N));
        menu.add(createMenuItem("Open", menuHandler::menuItemOpenSelected, KeyEvent.VK_O));
        menu.addSeparator();
        menu.add(createMenuItem("Close", menuHandler::menuItemCloseSelected, KeyEvent.VK_W));
        menu.add(createMenuItem("Save", menuHandler::menuItemSaveSelected, KeyEvent.VK_S));
        return menu;
    }

    private JMenu createEditMenu() {
        JMenu menu = new JMenu("Edit");
        undo = createMenuItem("Undo", menuHandler::menuItemUndoSelected, KeyEvent.VK_Z);
        menu.add(undo);
        redo = createMenuItem("Redo", menuHandler::menuItemRedoSelected, KeyEvent.VK_Z, SHIFT_DOWN_MASK);
        menu.add(redo);
        menu.addSeparator();
        cut = createMenuItem("Cut", menuHandler::menuItemCutSelected, KeyEvent.VK_X);
        menu.add(cut);
        copy = createMenuItem("Copy", menuHandler::menuItemCopySelected, KeyEvent.VK_C);
        menu.add(copy);
        menu.add(createMenuItem("Paste", menuHandler::menuItemPasteSelected, KeyEvent.VK_V));
        menu.add(createMenuItem("Select All", menuHandler::menuItemSelectAllSelected, KeyEvent.VK_A));
        return menu;
    }

    private Component createViewMenu() {
        JMenu menu = new JMenu("View");
        errors = new JCheckBoxMenuItem("Errors");
        errors.addActionListener(menuHandler::menuItemErrorsSelected);
        errors.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ACCELERATOR));
        menu.add(errors);
        nextError = createMenuItem("Jump to Next Error", menuHandler::menuItemNextErrorSelected,
                KeyEvent.VK_L, SHIFT_DOWN_MASK);
        menu.add(nextError);
        previousError = createMenuItem("Jump to Previous Error", menuHandler::menuItemPreviousErrorSelected,
                KeyEvent.VK_L, SHIFT_DOWN_MASK | InputEvent.ALT_DOWN_MASK);
        menu.add(previousError);
        return menu;
    }

    void setCopyCutEnabled(boolean enabled) {
        cut.setEnabled(enabled);
        copy.setEnabled(enabled);
    }

    void setUndoTitle(Optional<String> undoTitle) {
        undo.setEnabled(undoTitle.isPresent());
        undo.setText(undoTitle.orElse("Undo"));
    }

    void setRedoTitle(Optional<String> redoTitle) {
        redo.setEnabled(redoTitle.isPresent());
        redo.setText(redoTitle.orElse("Redo"));
    }

    void setErrorsVisible(boolean visible) {
        errors.setSelected(visible);
    }

    void setErrorNavigationEnabled(boolean enabled) {
        nextError.setEnabled(enabled);
        previousError.setEnabled(enabled);
    }

    interface MenuHandler {
        default void menuItemNewSelected(ActionEvent e) {
        }
        default void menuItemOpenSelected(ActionEvent e) {
        }
        default void menuItemCloseSelected(ActionEvent e) {
        }
        default void menuItemSaveSelected(ActionEvent e) {
        }

        default void menuItemUndoSelected(ActionEvent e) {
        }
        default void menuItemRedoSelected(ActionEvent e) {
        }
        default void menuItemCutSelected(ActionEvent e) {
        }
        default void menuItemCopySelected(ActionEvent e) {
        }
        default void menuItemPasteSelected(ActionEvent e) {
        }
        default void menuItemSelectAllSelected(ActionEvent e) {
        }
        default void menuItemErrorsSelected(ActionEvent e) {
        }
        default void menuItemNextErrorSelected(ActionEvent e) {
        }
        default void menuItemPreviousErrorSelected(ActionEvent e) {
        }
    }
}
