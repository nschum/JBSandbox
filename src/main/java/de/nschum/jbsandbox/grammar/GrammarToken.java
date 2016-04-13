package de.nschum.jbsandbox.grammar;

public enum GrammarToken {
    EXPR,
    EXPR_CONTINUED,
    OP,
    STMT,
    PROGRAM,
    PROGRAM_CONTINUED,

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
    KEYWORD_PRINT,
}
