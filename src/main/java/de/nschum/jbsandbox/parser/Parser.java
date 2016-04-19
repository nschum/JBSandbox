package de.nschum.jbsandbox.parser;

import de.nschum.jbsandbox.SourceLocation;
import de.nschum.jbsandbox.grammar.Grammar;
import de.nschum.jbsandbox.grammar.GrammarRule;
import de.nschum.jbsandbox.grammar.GrammarToken;
import de.nschum.jbsandbox.scanner.ScannerToken;

import java.util.*;

import static de.nschum.jbsandbox.grammar.Grammar.EOF;
import static de.nschum.jbsandbox.grammar.Grammar.EPSILON;

/**
 * Parses a Grammar
 * <p>
 * LL(1) parser for grammar, creates a ParseTree from a list of tokens.
 */
public class Parser {

    private Grammar grammar;
    private ParserTable parserTable;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        ParserFirstSets parserFirstSets = new ParserFirstSets(grammar);
        parserTable = new ParserTable(grammar, parserFirstSets, new ParserFollowSets(grammar, parserFirstSets));
    }

    public ParserTree parse(List<ScannerToken> tokens) throws UnexpectedTokenException, MissingTokenException {

        // stack of started rules / tree output
        Stack<ParserTree> stack = new Stack<>();
        final ParserTree rootTree = new ParserTree(grammar.getStartSymbol());
        stack.push(rootTree);

        Iterator<ScannerToken> unreadTokens = iterateWithEOF(tokens);
        ScannerToken nextToken = unreadTokens.next();

        while (stack.size() > 0 && !stack.peek().getToken().equals(EOF)) {
            if (stack.peek().getToken().equals(EPSILON)) {
                // skip epsilon
                ParserTree output = stack.pop();
                output.setContent(Optional.of(""));
                output.setLocation(nextToken.getLocation());
            } else if (stack.peek().getToken().equals(nextToken.getGrammarToken())) {
                // process terminal and remove it from input
                ParserTree output = stack.pop();
                output.setContent(Optional.of(nextToken.getContent()));
                output.setLocation(nextToken.getLocation());
                nextToken = unreadTokens.next();
            } else {
                // apply rule and rewrite stack
                ParserTree tree = stack.pop();

                GrammarRule rule = lookUpRule(nextToken, tree.getToken());

                for (GrammarToken token : reverse(rule.getRightHandSide())) {
                    tree.setRule(rule);
                    ParserTree newTree = new ParserTree(token);
                    tree.addChild(newTree);
                    stack.push(newTree);
                }
            }
        }

        if (unreadTokens.hasNext()) {
            throw new UnexpectedTokenException(nextToken);
        }

        return rootTree;
    }

    private GrammarRule lookUpRule(ScannerToken nextToken, GrammarToken state) throws MissingTokenException, UnexpectedTokenException {
        Optional<GrammarRule> optionalRule = parserTable.getRuleForTerminal(nextToken.getGrammarToken(), state);
        if (!optionalRule.isPresent()) {
            if (nextToken.getGrammarToken().equals(EOF)) {
                throw new MissingTokenException(state);
            } else {
                throw new UnexpectedTokenException(nextToken);
            }
        }
        return optionalRule.get();
    }

    /**
     * Iterate over tokens with EOF appended.
     */
    private Iterator<ScannerToken> iterateWithEOF(List<ScannerToken> tokens) {
        List<ScannerToken> tokensWithEOF = new ArrayList<>(tokens);
        tokensWithEOF.add(new ScannerToken(EOF, "", new SourceLocation(0, 0)));
        return tokensWithEOF.iterator();
    }

    private <T> List<T> reverse(List<T> list) {
        ArrayList<T> reverseList = new ArrayList<>(list);
        Collections.reverse(reverseList);
        return reverseList;
    }
}
