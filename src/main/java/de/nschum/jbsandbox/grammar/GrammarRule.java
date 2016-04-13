package de.nschum.jbsandbox.grammar;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * A rule for a language grammar
 */
public class GrammarRule {

    private GrammarToken leftHandSide;
    private List<GrammarToken> rightHandSide;

    public GrammarRule(GrammarToken leftHandSide, List<GrammarToken> rightHandSide) {
        this.leftHandSide = leftHandSide;
        this.rightHandSide = rightHandSide;
    }

    public GrammarRule(GrammarToken leftHandSide, GrammarToken... rightHandSide) {
        this(leftHandSide, Arrays.asList(rightHandSide));
    }

    @Override
    public String toString() {
        return "GrammarRule{"
                + leftHandSide
                + " ::= "
                + rightHandSide.stream().map(GrammarToken::toString).collect(joining(" "))
                + '}';
    }
}
