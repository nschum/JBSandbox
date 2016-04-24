package de.nschum.jbsandbox.ui;

import de.nschum.jbsandbox.source.SourceFile;
import de.nschum.jbsandbox.source.SourceRange;

import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.util.List;

import static java.awt.BasicStroke.CAP_BUTT;
import static java.awt.BasicStroke.JOIN_BEVEL;

/**
 * Modifies a StyledDocument by adding attributes for errors
 */
public class ErrorHighlighter {

    private final Highlighter highlighter;
    private final Highlighter.HighlightPainter painter = new WavyLineHighlightPainter(new Color(255, 81, 81));

    public ErrorHighlighter(Highlighter highlighter) {
        this.highlighter = highlighter;
        assert highlighter != null;
    }

    public void highlightErrors(SourceFile sourceFile, List<ParseError> errors) {
        removeAllHighlights();
        for (ParseError error : errors) {
            SourceRange location = error.getLocation();
            int start = sourceFile.offsetForLocation(location.getStart());
            int end = location.isSinglePoint() ? start + 1 : sourceFile.offsetForLocation(location.getEnd());
            highlightRange(start, end);
        }
    }

    public void removeAllHighlights() {
        highlighter.removeAllHighlights();
    }


    private void highlightRange(int start, int end) {
        try {
            highlighter.addHighlight(start, end, painter);
        } catch (BadLocationException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Highlights the text between two document locations line by line
     * <p/>
     * Highlights only up to the end of the text for each line, not to the right margin.
     */
    private static abstract class LineHighlightPainter implements Highlighter.HighlightPainter {

        private final Color color;
        protected final int thickness = 2;

        public LineHighlightPainter(Color color) {
            this.color = color;
        }

        @Override
        public void paint(Graphics graphics, int from, int to, Shape boundsShape, JTextComponent c) {

            final Graphics2D g = (Graphics2D) graphics.create();
            try {
                g.setColor(color);
                g.setStroke(new BasicStroke(thickness));
                for (int location = from; location < to; location++) {
                    drawForCharacter(g, location, c);
                }
            } catch (BadLocationException e) {
                throw new AssertionError(e);
            } finally {
                g.dispose();
            }
        }

        private void drawForCharacter(Graphics2D g, int location, JTextComponent c) throws BadLocationException {
            TextUI mapper = c.getUI();
            Rectangle before = mapper.modelToView(c, location, Position.Bias.Forward);
            Rectangle after = mapper.modelToView(c, location + 1, Position.Bias.Backward);
            Rectangle character = before.union(after);
            if (character.width == 0) {
                // to still draw something at EOF
                character.width = Math.max(character.width, 10);
            }
            g.setClip(character);
            underline(g, character.x, character.y, character.width, character.height);
        }

        protected abstract void underline(Graphics2D g, int x, int y, int width, int height);
    }

    private static class DashedLineHighlightPainter extends LineHighlightPainter {

        public DashedLineHighlightPainter(Color color) {
            super(color);
        }

        protected void underline(Graphics2D g, int x, int y, int width, int height) {
            int baseline = y + height - 1;

            g.setStroke(new BasicStroke(thickness, CAP_BUTT, JOIN_BEVEL, 0, new float[]{3, 1}, x));
            g.drawLine(x, baseline, x + width, baseline);
        }
    }

    private static class WavyLineHighlightPainter extends LineHighlightPainter {

        private final int waveLength = 2;
        private final int amplitude = 2;

        public WavyLineHighlightPainter(Color color) {
            super(color);
        }

        protected void underline(Graphics2D g, int x, int y, int width, int height) {
            double baseline = y + height - 1.5;

            for (int start = x / waveLength * waveLength; start < x + width; start += waveLength) {
                int amplitude = start / waveLength % 2 == 0 ? this.amplitude : -this.amplitude;
                g.draw(new QuadCurve2D.Double(
                        start, baseline,
                        start + waveLength * 0.5, baseline + amplitude,
                        start + waveLength, baseline));
            }
        }
    }
}
