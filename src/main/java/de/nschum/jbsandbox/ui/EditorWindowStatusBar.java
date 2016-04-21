package de.nschum.jbsandbox.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * The status bar for the main editor window
 */
public class EditorWindowStatusBar extends JPanel {

    private final StatusBarButton status;

    public EditorWindowStatusBar() {
        setPreferredSize(new Dimension(0, 22));
        setBorder(new EmptyBorder(0, 8, 0, 8));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        status = new StatusBarButton("");
        status.setHorizontalAlignment(SwingConstants.LEFT);
        add(status);
    }

    public String getText() {
        return status.getText();
    }

    public void setText(String text) {
        status.setText(text);
    }
}
