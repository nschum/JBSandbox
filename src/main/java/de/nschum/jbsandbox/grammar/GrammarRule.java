package de.nschum.jbsandbox.grammar;

import java.util.Arrays;
import java.util.List;

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
}
