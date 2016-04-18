package de.nschum.jbsandbox.parser;

import de.nschum.jbsandbox.grammar.GrammarToken;

import static de.nschum.jbsandbox.grammar.GrammarToken.nonTerminal;
import static de.nschum.jbsandbox.grammar.GrammarToken.terminal;

public class MockTokens {

    public static final GrammarToken S = nonTerminal("S");
    public static final GrammarToken A = nonTerminal("A");
    public static final GrammarToken B = nonTerminal("B");
    public static final GrammarToken C = nonTerminal("C");
    public static final GrammarToken D = nonTerminal("D");
    public static final GrammarToken E = nonTerminal("E");
    public static final GrammarToken F = nonTerminal("F");
    public static final GrammarToken a = terminal("a");
    public static final GrammarToken b = terminal("b");
    public static final GrammarToken c = terminal("c");
    public static final GrammarToken d = terminal("d");
}
