package de.nschum.jbsandbox.grammar;

public class GrammarToken {

    private String name;
    private boolean terminal;

    public static GrammarToken terminal(String name) {
        return new GrammarToken(name, true);
    }

    public static GrammarToken nonTerminal(String name) {
        return new GrammarToken(name, false);
    }

    private GrammarToken(String name, boolean terminal) {
        assert name != null;
        this.name = name;
        this.terminal = terminal;
    }

    public String getName() {
        return name;
    }

    /**
     * Return if this is a token is a terminal, i.e. appears in the scanned input
     * <p>
     * A non-terminal is only used in the grammar.
     */
    public boolean isTerminal() {
        return terminal;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GrammarToken grammarToken = (GrammarToken) o;

        return terminal == grammarToken.terminal && name == grammarToken.name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
