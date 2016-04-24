package de.nschum.jbsandbox.ui;

import de.nschum.jbsandbox.ast.FloatLiteral;
import de.nschum.jbsandbox.ast.IntLiteral;
import de.nschum.jbsandbox.ast.Terminal;
import de.nschum.jbsandbox.grammar.GrammarToken;
import de.nschum.jbsandbox.source.SourceFile;
import de.nschum.jbsandbox.source.SourceRange;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static de.nschum.jbsandbox.grammar.JBGrammar.*;

/**
 * Applies syntax colors to the document according to the SyntaxTree
 */
public class SyntaxHighlighter {

    private final StyledDocument document;

    private final Style literalStyle;
    private final Style plainStyle;

    private final Map<GrammarToken, Style> tokenStyles = new HashMap<>();

    public SyntaxHighlighter(StyledDocument document) {
        this.document = document;

        plainStyle = document.addStyle("plain", null);
        StyleConstants.setFontFamily(plainStyle, "monospaced");
        StyleConstants.setForeground(plainStyle, new Color(66, 66, 66));

        literalStyle = document.addStyle("literal", plainStyle);
        StyleConstants.setForeground(literalStyle, new Color(74, 111, 255));

        Style stringStyle = document.addStyle("string", plainStyle);
        StyleConstants.setForeground(stringStyle, new Color(74, 111, 255));

        Style identifierStyle = document.addStyle("identifier", plainStyle);
        StyleConstants.setForeground(identifierStyle, new Color(33, 154, 60));

        Style operatorStyle = document.addStyle("operator", plainStyle);
        StyleConstants.setForeground(operatorStyle, new Color(255, 86, 5));
        StyleConstants.setBold(operatorStyle, true);

        Style keywordStyle = document.addStyle("keyword", plainStyle);
        StyleConstants.setForeground(keywordStyle, new Color(166, 66, 66));

        tokenStyles.put(NUMBER, literalStyle);
        tokenStyles.put(STRING, stringStyle);
        tokenStyles.put(IDENTIFIER, identifierStyle);

        tokenStyles.put(PAREN_OPEN, operatorStyle);
        tokenStyles.put(PAREN_CLOSE, operatorStyle);
        tokenStyles.put(BRACE_OPEN, operatorStyle);
        tokenStyles.put(BRACE_CLOSE, operatorStyle);
        tokenStyles.put(COMMA, operatorStyle);
        tokenStyles.put(ARROW, operatorStyle);
        tokenStyles.put(PLUS, operatorStyle);
        tokenStyles.put(MINUS, operatorStyle);
        tokenStyles.put(STAR, operatorStyle);
        tokenStyles.put(SLASH, operatorStyle);
        tokenStyles.put(HAT, operatorStyle);
        tokenStyles.put(EQUALS, operatorStyle);

        tokenStyles.put(KEYWORD_VAR, keywordStyle);
        tokenStyles.put(KEYWORD_MAP, keywordStyle);
        tokenStyles.put(KEYWORD_REDUCE, keywordStyle);
        tokenStyles.put(KEYWORD_OUT, keywordStyle);
        tokenStyles.put(KEYWORD_PRINT, keywordStyle);

    }

    public void highlight(ParseResult parseResult) {
        if (!parseResult.getSyntaxTree().isPresent()) {
            removeAllHighlights();
            return;
        }

        final SourceFile sourceFile = parseResult.getSourceFile();
        SwingUtilities.invokeLater(() -> {
            parseResult.getSyntaxTree().get().visit(node -> {
                if (node instanceof FloatLiteral || node instanceof IntLiteral) {
                    highlightRange(sourceFile, node.getLocation(), literalStyle);
                } else if (node instanceof Terminal) {
                    GrammarToken token = ((Terminal) node).getToken();
                    highlightRange(sourceFile, node.getLocation(), tokenStyles.get(token));
                }
            });
        });
    }

    private void highlightRange(SourceFile sourceFile, SourceRange range, Style style) {
        int start = sourceFile.offsetForLocation(range.getStart());
        int end = sourceFile.offsetForLocation(range.getEnd());
        document.setCharacterAttributes(start, end - start, style, false);
    }

    public void removeAllHighlights() {
        document.setCharacterAttributes(0, document.getLength(), plainStyle, true);
    }
}
