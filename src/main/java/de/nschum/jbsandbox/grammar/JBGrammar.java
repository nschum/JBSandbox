package de.nschum.jbsandbox.grammar;

import java.util.ArrayList;
import java.util.List;

import static de.nschum.jbsandbox.grammar.GrammarToken.*;

public class JBGrammar implements Grammar {

    private List<GrammarRule> rules = new ArrayList<>();

    public JBGrammar() {
        addRule(EXPR, PAREN_OPEN, EXPR, PAREN_CLOSE, EXPR_CONTINUED);
        addRule(EXPR, IDENTIFIER, EXPR_CONTINUED);
        addRule(EXPR, BRACE_OPEN, EXPR, COMMA, EXPR, BRACE_CLOSE, EXPR_CONTINUED);
        addRule(EXPR, NUMBER, EXPR_CONTINUED);
        addRule(EXPR, KEYWORD_MAP, PAREN_OPEN, EXPR, COMMA, IDENTIFIER, ARROW, EXPR, PAREN_CLOSE, EXPR_CONTINUED);
        addRule(EXPR, KEYWORD_REDUCE, PAREN_OPEN, EXPR, COMMA, EXPR, COMMA, IDENTIFIER, IDENTIFIER, ARROW, EXPR, PAREN_CLOSE, EXPR_CONTINUED);

        addRule(EXPR_CONTINUED, OP, EXPR);
        addRule(EXPR_CONTINUED, EPSILON);

        addRule(OP, PLUS);
        addRule(OP, MINUS);
        addRule(OP, STAR);
        addRule(OP, SLASH);
        addRule(OP, HAT);

        addRule(STMT, KEYWORD_VAR, IDENTIFIER, EQUALS, EXPR);
        addRule(STMT, KEYWORD_OUT, EXPR);
        addRule(STMT, KEYWORD_PRINT, STRING);

        addRule(PROGRAM, STMT, PROGRAM_CONTINUED);

        addRule(PROGRAM_CONTINUED, STMT, PROGRAM_CONTINUED);
        addRule(PROGRAM_CONTINUED, EOF);
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
