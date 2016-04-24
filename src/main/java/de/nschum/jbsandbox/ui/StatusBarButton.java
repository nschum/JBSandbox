package de.nschum.jbsandbox.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

/**
 * A button styled to fit into a status bar
 */
public class StatusBarButton extends JButton {

    public static final float FONT_SIZE = 10f;
    public static final Color COLOR = new Color(96, 96, 96);

    private MouseAdapter mouseListener;
    private boolean clickable = false;

    public StatusBarButton(String text) {
        super(text);

        Font regularFont = getFont().deriveFont(FONT_SIZE);

        Map<TextAttribute, Object> highlightAttributes = new HashMap<>();
        highlightAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_ONE_PIXEL);
        Font highlightFont = regularFont.deriveFont(highlightAttributes);

        setFont(regularFont);
        setForeground(COLOR);
        setBorder(new EmptyBorder(4, 0, 4, 0));
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        mouseListener = new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                setFont(highlightFont);
            }

            public void mouseExited(MouseEvent evt) {
                setFont(regularFont);
            }
        };
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        if (clickable != this.clickable) {
            if (clickable) {
                addMouseListener(mouseListener);
            } else {
                removeMouseListener(mouseListener);
            }
        }
        this.clickable = clickable;
    }
}
