package de.nschum.jbsandbox.scanner;

import static de.nschum.jbsandbox.grammar.GrammarToken.*;

public class JBScanner extends Scanner {

    public JBScanner() {
        registerOperator("(", PAREN_OPEN);
        registerOperator(")", PAREN_CLOSE);
        registerOperator("{", BRACE_OPEN);
        registerOperator("}", BRACE_CLOSE);
        registerOperator(",", COMMA);
        registerOperator("->", ARROW);
        registerOperator("+", PLUS);
        registerOperator("-", MINUS);
        registerOperator("*", STAR);
        registerOperator("/", SLASH);
        registerOperator("^", HAT);
        registerOperator("=", EQUALS);

        registerKeyword("var", KEYWORD_VAR);
        registerKeyword("map", KEYWORD_MAP);
        registerKeyword("out", KEYWORD_OUT);
        registerKeyword("print", KEYWORD_PRINT);
        registerKeyword("reduce", KEYWORD_REDUCE);

        registerPattern("\"[^\"]*\"", STRING);
        registerPattern("[a-zA-Z][a-zA-Z0-9]*", IDENTIFIER);
        registerPattern("[0-9]+(?:\\.[0-9]+)?" + Scanner.NON_WORD_LOOKAHEAD, NUMBER);

        compile();
    }
}
