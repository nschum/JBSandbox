package de.nschum.jbsandbox.parser;

import de.nschum.jbsandbox.grammar.GrammarRule;
import de.nschum.jbsandbox.grammar.GrammarToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

/**
 * Parser output in tree form
 * <p>
 * Each rule results in a node in the ParserTree.
 */
public class ParserTree {

    private GrammarRule rule;
    private GrammarToken token;
    private Optional<String> content = Optional.empty();
    private List<ParserTree> children = new ArrayList<>();

    public ParserTree(GrammarToken token) {
        this.token = token;
    }

    void addChild(ParserTree child) {
        children.add(0, child);
    }

    void setRule(GrammarRule rule) {
        this.rule = rule;
    }

    void setContent(Optional<String> content) {
        this.content = content;
    }

    public GrammarRule getRule() {
        return rule;
    }

    public GrammarToken getToken() {
        return token;
    }

    public Optional<String> getContent() {
        return content;
    }

    public List<ParserTree> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return toString("");
    }

    private String toString(String indent) {
        return indent + "ParseTree{"
                + content + " = "
                + token + " rule " + rule + "\n"
                + children.stream().map(c -> c.toString(indent + "  ")).collect(joining("\n"))
                + '}';
    }
}
