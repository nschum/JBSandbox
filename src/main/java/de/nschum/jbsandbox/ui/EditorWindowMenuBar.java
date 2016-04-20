package de.nschum.jbsandbox.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Menu for main editor window
 */
class EditorWindowMenuBar extends JMenuBar {

    private static int ACCELERATOR = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    private MenuHandler menuHandler;
    private JMenuItem cut;
    private JMenuItem copy;

    EditorWindowMenuBar(MenuHandler menuHandler) {
        assert menuHandler != null;

        this.menuHandler = menuHandler;

        add(createFileMenu());
        add(createEditMenu());

        setCopyCutEnabled(false);
    }

    private JMenuItem createMenuItem(String text, ActionListener actionListener, int key) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(actionListener);
        item.setAccelerator(KeyStroke.getKeyStroke(key, ACCELERATOR));
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
        cut = createMenuItem("Cut", menuHandler::menuItemCutSelected, KeyEvent.VK_X);
        menu.add(cut);
        copy = createMenuItem("Copy", menuHandler::menuItemCopySelected, KeyEvent.VK_C);
        menu.add(copy);
        menu.add(createMenuItem("Paste", menuHandler::menuItemPasteSelected, KeyEvent.VK_V));
        menu.add(createMenuItem("Select All", menuHandler::menuItemSelectAllSelected, KeyEvent.VK_A));
        return menu;
    }

    void setCopyCutEnabled(boolean enabled) {
        cut.setEnabled(enabled);
        copy.setEnabled(enabled);
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

        default void menuItemCutSelected(ActionEvent e) {
        }
        default void menuItemCopySelected(ActionEvent e) {
        }
        default void menuItemPasteSelected(ActionEvent e) {
        }
        default void menuItemSelectAllSelected(ActionEvent e) {
        }
    }
}
