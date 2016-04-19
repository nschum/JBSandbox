package de.nschum.jbsandbox.parser;

import de.nschum.jbsandbox.SourceLocation;
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
    private Optional<SourceLocation> location = Optional.empty();
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

    void setLocation(SourceLocation location) {
        this.location = Optional.of(location);
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

    public SourceLocation getLocation() {
        return location.orElseGet(() -> children.get(0).getLocation());
    }

    public ParserTree getChild(int i) {
        return children.get(i);
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
