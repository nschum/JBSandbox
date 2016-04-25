package de.nschum.jbsandbox.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Output area for program errors and results
 */
public class LogTextArea extends JTextPane {

    private static final String ATTRIBUTE_ERROR = "error";
    private static final String ATTRIBUTE_CURSOR = "cursor";
    private final Style plainStyle;
    private final Style selectableStyle;

    private List<Consumer<EditorError>> selectionListeners = new ArrayList<>();

    public LogTextArea() {
        setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        setEditable(false);
        setBackground(Color.LIGHT_GRAY);
        setBorder(new EmptyBorder(0, 0, 0, 0));

        plainStyle = addStyle("plain", null);
        StyleConstants.setFontFamily(plainStyle, "monospaced");

        selectableStyle = addStyle("selectable", plainStyle);
        StyleConstants.setUnderline(selectableStyle, true);
        selectableStyle.addAttribute(ATTRIBUTE_CURSOR, Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getMouseEventAttribute(e, ATTRIBUTE_ERROR, EditorError.class).ifPresent(error -> {
                    selectionListeners.forEach(listener -> listener.accept(error));
                });
            }
        });
    }

    /**
     * Add observer for the user selecting an error
     */
    public void addSelectionListener(Consumer<EditorError> listener) {
        selectionListeners.add(listener);
    }

    @Override
    public void setText(String t) {
        super.setText(t);
        getStyledDocument().setCharacterAttributes(0, getStyledDocument().getLength(), plainStyle, true);
    }

    /**
     * Set the list of displayed errors
     */
    public void setErrors(List<EditorError> errors) {
        setText("");
        try {
            for (EditorError error : errors) {
                appendError(error);
            }
        } catch (BadLocationException e) {
            throw new AssertionError(e);
        }
    }

    private void appendError(EditorError error) throws BadLocationException {
        append(error.getMessage() + ": ", plainStyle);

        SimpleAttributeSet attributes = new SimpleAttributeSet(selectableStyle);
        attributes.addAttribute(ATTRIBUTE_ERROR, error);
        append(error.getLocation().toHumanReadableString(), attributes);

        append("\n", plainStyle);
    }

    private void append(String string, AttributeSet attributeSet) throws BadLocationException {
        int offset = getText().length();
        getStyledDocument().insertString(offset, string, attributeSet);
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent event) {
        super.processMouseMotionEvent(event);
        setCursor(getMouseEventAttribute(event, ATTRIBUTE_CURSOR, Cursor.class)
                .orElse(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR)));
    }

    private <T> Optional<T> getMouseEventAttribute(MouseEvent e, String attribute, Class<T> clazz) {
        int position = viewToModel(e.getPoint());
        AttributeSet attributes = getStyledDocument().getCharacterElement(position).getAttributes();
        @SuppressWarnings("unchecked")
        T value = (T) attributes.getAttribute(attribute);
        return Optional.ofNullable(value);
    }
}
