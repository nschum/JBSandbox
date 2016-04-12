package de.nschum.jbsandbox.grammar;

import java.util.List;

/**
 * A language grammar
 */
public interface Grammar {

    /**
     * Return all grammar rules
     */
    List<GrammarRule> getRules();

    /**
     * Return the grammar's start symbol
     */
    GrammarToken getStartSymbol();
}
