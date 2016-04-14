package de.nschum.jbsandbox.grammar;

public enum GrammarToken {
    EXPR(false),
    EXPR_CONTINUED(false),
    OP(false),
    STMT(false),
    PROGRAM(false),
    PROGRAM_CONTINUED(false),

    // shortcuts
    NUMBER,
    STRING,
    IDENTIFIER,

    // terminal symbols
    EPSILON, // empty word
    EOF, // end of input
    PAREN_OPEN, // (
    PAREN_CLOSE, // )
    BRACE_OPEN, // {
    BRACE_CLOSE, // }
    COMMA, // ,
    ARROW, // ->
    PLUS, // +
    MINUS, // -
    STAR, // *
    SLASH, // /
    HAT, // ^
    EQUALS, // =
    KEYWORD_VAR,
    KEYWORD_MAP,
    KEYWORD_REDUCE,
    KEYWORD_OUT,
    KEYWORD_PRINT;

    private boolean terminal;

    GrammarToken(boolean terminal) {
        this.terminal = terminal;
    }

    GrammarToken() {
        this(true);
    }

    /**
     * Return if this is a token is a terminal, i.e. appears in the scanned input
     * <p>
     * A non-terminal is only used in the grammar.
     */
    public boolean isTerminal() {
        return terminal;
    }
}
