package de.nschum.jbsandbox.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The status bar for the main editor window
 */
public class EditorWindowStatusBar extends JPanel {

    private final StatusBarButton status;
    private final StatusBarButton error;

    public EditorWindowStatusBar() {
        setPreferredSize(new Dimension(0, 22));
        setBorder(new EmptyBorder(0, 8, 0, 8));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        status = new StatusBarButton("");
        status.setHorizontalAlignment(SwingConstants.LEFT);
        add(status);

        add(Box.createGlue());

        error = new StatusBarButton("0 errors");
        error.setClickable(true);
        error.setHorizontalAlignment(SwingConstants.RIGHT);
        setErrorSelected(false);
        add(error);
    }

    public void addErrorActionListener(ActionListener actionListener) {
        error.addActionListener(actionListener);
    }

    public void setErrorSelected(boolean errorSelected) {
        error.setForeground(errorSelected ? StatusBarButton.COLOR : new Color(160, 0, 0));
    }

    public String getText() {
        return status.getText();
    }

    public void setText(String text) {
        status.setText(text);
    }
}
