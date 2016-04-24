package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.grammar.GrammarRule;
import de.nschum.jbsandbox.grammar.JBGrammar;
import de.nschum.jbsandbox.parser.ParserTree;

import java.util.Stack;

import static de.nschum.jbsandbox.grammar.JBGrammar.*;

/**
 * Helper for ASTBuilder that parses statements
 */
public class ASTStatementBuilder extends ASTBaseBuilder {

    public ASTStatementBuilder(JBGrammar grammar) {
        super(grammar);
    }

    Statement parseStatement(ParserTree parserTree, Stack<Scope> scopes) {
        GrammarRule rule = parserTree.getRule();
        Scope scope = scopes.peek();
        Statement result;

        JBGrammar grammar = getGrammar();
        if (rule == grammar.ruleDeclaration) {
            result = parseRuleDeclaration(parserTree, scopes, scope);
        } else if (rule == grammar.ruleOutStatement) {
            result = parseRuleOutStatement(parserTree, scope);
        } else if (rule == grammar.rulePrintStatement) {
            result = parseRulePrintStatement(parserTree);
        } else {
            throw new IllegalStateException("Unknown rule");
        }

        return result;
    }

    private Statement parseRuleDeclaration(ParserTree parserTree, Stack<Scope> scopes, Scope scope) {
        assertRule(parserTree, KEYWORD_VAR, IDENTIFIER, EQUALS, EXPR);

        ParserTree variableTree = parserTree.getChildren().get(1);
        ParserTree expressionTree = parserTree.getChildren().get(3);

        Expression expression = parseExpression(expressionTree, scope);
        Variable variable = new Variable(expression.getType(), variableTree.getContent().get());

        // Add new variable to scope for following statements.
        scopes.push(scope.addVariable(variable));

        return new Declaration(variable, expression, parseTerminals(parserTree), parserTree.getLocation());
    }

    private Statement parseRuleOutStatement(ParserTree parserTree, Scope scope) {
        assertRule(parserTree, KEYWORD_OUT, EXPR);

        ParserTree expressionTree = parserTree.getChild(1);

        return new OutStatement(parseExpression(expressionTree, scope),
                parseTerminals(parserTree), parserTree.getLocation());
    }

    private Statement parseRulePrintStatement(ParserTree parserTree) {
        assertRule(parserTree, KEYWORD_PRINT, STRING);

        ParserTree stringTree = parserTree.getChild(1);

        return new PrintStatement(parseString(stringTree), parseTerminals(parserTree), parserTree.getLocation());
    }

    private Expression parseExpression(ParserTree parserTree, Scope scope) {
        final ASTExpressionBuilder expressionBuilder = new ASTExpressionBuilder(getGrammar());
        final Expression expression = expressionBuilder.parseExpression(parserTree, scope);
        expressionBuilder.getErrors().forEach(this::reportError);
        return expression;
    }

    private String parseString(ParserTree stringTree) {
        String content = stringTree.getContent().get();
        // strip quotes
        return content.substring(1, content.length() - 1);
    }
}
