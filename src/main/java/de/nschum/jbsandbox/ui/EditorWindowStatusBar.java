package de.nschum.jbsandbox.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Optional;

/**
 * The status bar for the main editor window
 */
public class EditorWindowStatusBar extends JPanel {

    private final StatusBarButton status;
    private final StatusBarButton location;
    private final StatusBarButton log;

    private Color logButtonColor;
    private boolean logSelected = false;

    public EditorWindowStatusBar() {
        setPreferredSize(new Dimension(0, 22));
        setBorder(new EmptyBorder(0, 8, 0, 8));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        location = new StatusBarButton("");
        location.setHorizontalAlignment(SwingConstants.RIGHT);
        add(location);

        add(Box.createHorizontalStrut(10));

        status = new StatusBarButton("");
        status.setHorizontalAlignment(SwingConstants.LEFT);
        add(status);

        add(Box.createGlue());

        log = new StatusBarButton("");
        log.setClickable(true);
        log.setHorizontalAlignment(SwingConstants.RIGHT);
        setLogSelected(false);
        add(log);
    }

    public void addErrorActionListener(ActionListener actionListener) {
        log.addActionListener(actionListener);
    }

    public void setParsing() {
        log.setText("parsing…");
        logButtonColor = new Color(0, 0, 160);
    }

    public void setLogButtonOutput(int errorCount, Optional<String> output) {
        switch (errorCount) {
            case 0:
                log.setText("running…");
                break;
            case 1:
                log.setText("1 error");
                break;
            default:
                log.setText(errorCount + " errors");
        }

        if (errorCount == 0) {
            output.ifPresent(o -> {
                int lineCount = o.split("\n").length;
                if (o.length() == 0) {
                    log.setText("no output");
                } else if (lineCount == 1) {
                    log.setText("1 line of output");
                } else {
                    log.setText(lineCount + " lines of output");
                }
            });
        }

        if (errorCount > 0) {
            logButtonColor = new Color(160, 0, 0);
        } else {
            logButtonColor = new Color(0, 160, 0);
        }
        updateLogButtonColor();
    }

    public void setLogSelected(boolean logSelected) {
        this.logSelected = logSelected;
        updateLogButtonColor();
    }

    private void updateLogButtonColor() {
        log.setForeground(logSelected ? StatusBarButton.COLOR : logButtonColor);
    }

    public String getText() {
        return status.getText();
    }

    public void setText(String text) {
        status.setText(text);
    }

    public void setLocationText(String text) {
        location.setText(text);
    }
}
