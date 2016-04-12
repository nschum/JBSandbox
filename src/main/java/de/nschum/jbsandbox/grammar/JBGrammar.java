package de.nschum.jbsandbox.grammar;

import java.util.ArrayList;
import java.util.List;

import static de.nschum.jbsandbox.grammar.GrammarToken.*;

public class JBGrammar implements Grammar {

    private List<GrammarRule> rules = new ArrayList<>();

    public JBGrammar() {
        addRule(EXPR, EXPR, OP, EXPR);
        addRule(EXPR, PAREN_OPEN, EXPR, PAREN_CLOSE);
        addRule(EXPR, IDENTIFIER);
        addRule(EXPR, BRACE_OPEN, EXPR, COMMA, EXPR, BRACE_CLOSE);
        addRule(EXPR, NUMBER);
        addRule(EXPR, KEYWORD_MAP, PAREN_OPEN, EXPR, COMMA, IDENTIFIER, ARROW, EXPR, PAREN_CLOSE);
        addRule(EXPR, KEYWORD_REDUCE, PAREN_OPEN, EXPR, COMMA, EXPR, COMMA, IDENTIFIER, IDENTIFIER, ARROW, EXPR, PAREN_CLOSE);

        addRule(OP, PLUS);
        addRule(OP, MINUS);
        addRule(OP, STAR);
        addRule(OP, SLASH);
        addRule(OP, HAT);

        addRule(STMT, KEYWORD_VAR, IDENTIFIER, EQUALS, EXPR);
        addRule(STMT, KEYWORD_OUT, EXPR);
        addRule(STMT, KEYWORD_PRINT, STRING);

        addRule(PROGRAM, STMT);
        addRule(PROGRAM, PROGRAM, STMT);
    }

    private void addRule(GrammarToken leftHandSide, GrammarToken... rightHandSide) {
        rules.add(new GrammarRule(leftHandSide, rightHandSide));
    }

    @Override
    public List<GrammarRule> getRules() {
        return rules;
    }

    @Override
    public GrammarToken getStartSymbol() {
        return PROGRAM;
    }
}
