package de.nschum.jbsandbox.grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static de.nschum.jbsandbox.grammar.GrammarToken.nonTerminal;
import static de.nschum.jbsandbox.grammar.GrammarToken.terminal;
import static java.util.stream.Collectors.toSet;

public class JBGrammar implements Grammar {

    public static final GrammarToken EXPR = nonTerminal("EXPR");
    public static final GrammarToken EXPR_CONTINUED = nonTerminal("EXPR_CONTINUED");
    public static final GrammarToken OP = nonTerminal("OP");
    public static final GrammarToken STMT = nonTerminal("STMT");
    public static final GrammarToken PROGRAM = nonTerminal("PROGRAM");
    public static final GrammarToken PROGRAM_CONTINUED = nonTerminal("PROGRAM_CONTINUED");

    // shortcuts
    public static final GrammarToken NUMBER = terminal("NUMBER");
    public static final GrammarToken STRING = terminal("STRING");
    public static final GrammarToken IDENTIFIER = terminal("IDENTIFIER");

    // terminal symbols
    public static final GrammarToken PAREN_OPEN = terminal("PAREN_OPEN"); // (
    public static final GrammarToken PAREN_CLOSE = terminal("PAREN_CLOSE"); // )
    public static final GrammarToken BRACE_OPEN = terminal("BRACE_OPEN"); // {
    public static final GrammarToken BRACE_CLOSE = terminal("BRACE_CLOSE"); // }
    public static final GrammarToken COMMA = terminal("COMMA"); // ,
    public static final GrammarToken ARROW = terminal("ARROW"); // ->
    public static final GrammarToken PLUS = terminal("PLUS"); // +
    public static final GrammarToken MINUS = terminal("MINUS"); // -
    public static final GrammarToken STAR = terminal("STAR"); // *
    public static final GrammarToken SLASH = terminal("SLASH"); // /
    public static final GrammarToken HAT = terminal("HAT"); // ^
    public static final GrammarToken EQUALS = terminal("EQUALS"); // =
    public static final GrammarToken KEYWORD_VAR = terminal("KEYWORD_VAR");
    public static final GrammarToken KEYWORD_MAP = terminal("KEYWORD_MAP");
    public static final GrammarToken KEYWORD_REDUCE = terminal("KEYWORD_REDUCE");
    public static final GrammarToken KEYWORD_OUT = terminal("KEYWORD_OUT");
    public static final GrammarToken KEYWORD_PRINT = terminal("KEYWORD_PRINT");

    private List<GrammarRule> rules = new ArrayList<>();

    public final GrammarRule ruleParentheses = addRule(EXPR, PAREN_OPEN, EXPR, PAREN_CLOSE, EXPR_CONTINUED);
    public final GrammarRule ruleReference = addRule(EXPR, IDENTIFIER, EXPR_CONTINUED);
    public final GrammarRule ruleRange = addRule(EXPR, BRACE_OPEN, EXPR, COMMA, EXPR, BRACE_CLOSE, EXPR_CONTINUED);
    public final GrammarRule ruleNumber = addRule(EXPR, NUMBER, EXPR_CONTINUED);
    public final GrammarRule ruleMap = addRule(EXPR, KEYWORD_MAP, PAREN_OPEN, EXPR, COMMA, IDENTIFIER, ARROW, EXPR, PAREN_CLOSE, EXPR_CONTINUED);
    public final GrammarRule ruleReduce = addRule(EXPR, KEYWORD_REDUCE, PAREN_OPEN, EXPR, COMMA, EXPR, COMMA, IDENTIFIER, IDENTIFIER, ARROW, EXPR, PAREN_CLOSE, EXPR_CONTINUED);
    public final GrammarRule ruleNegate = addRule(EXPR, MINUS, NUMBER, EXPR_CONTINUED);

    public final GrammarRule ruleOperator = addRule(EXPR_CONTINUED, OP, EXPR);
    public final GrammarRule ruleNoOperator = addRule(EXPR_CONTINUED, EPSILON);

    public final GrammarRule rulePlus = addRule(OP, PLUS);
    public final GrammarRule ruleMinus = addRule(OP, MINUS);
    public final GrammarRule ruleMultiply = addRule(OP, STAR);
    public final GrammarRule ruleDivide = addRule(OP, SLASH);
    public final GrammarRule ruleExp = addRule(OP, HAT);

    public final GrammarRule ruleDeclaration = addRule(STMT, KEYWORD_VAR, IDENTIFIER, EQUALS, EXPR);
    public final GrammarRule ruleOutStatement = addRule(STMT, KEYWORD_OUT, EXPR);
    public final GrammarRule rulePrintStatement = addRule(STMT, KEYWORD_PRINT, STRING);

    public final GrammarRule ruleFirstStatement = addRule(PROGRAM, STMT, PROGRAM_CONTINUED);

    public final GrammarRule ruleFurtherStatements = addRule(PROGRAM_CONTINUED, STMT, PROGRAM_CONTINUED);
    public final GrammarRule ruleEof = addRule(PROGRAM_CONTINUED, EOF);

    private GrammarRule addRule(GrammarToken leftHandSide, GrammarToken... rightHandSide) {
        final GrammarRule rule = new GrammarRule(leftHandSide, rightHandSide);
        rules.add(rule);
        return rule;
    }

    @Override
    public List<GrammarRule> getRules() {
        return rules;
    }

    @Override
    public Set<GrammarToken> getTokens() {
        return Stream.of(EPSILON, EOF,
                EXPR, EXPR_CONTINUED, OP, STMT, PROGRAM, PROGRAM_CONTINUED,
                NUMBER, STRING, IDENTIFIER,
                PAREN_OPEN, PAREN_CLOSE, BRACE_OPEN, BRACE_CLOSE, COMMA, ARROW, PLUS, MINUS, STAR, SLASH, HAT, EQUALS,
                KEYWORD_VAR, KEYWORD_MAP, KEYWORD_REDUCE, KEYWORD_OUT, KEYWORD_PRINT).collect(toSet());
    }

    @Override
    public GrammarToken getStartSymbol() {
        return PROGRAM;
    }
}
