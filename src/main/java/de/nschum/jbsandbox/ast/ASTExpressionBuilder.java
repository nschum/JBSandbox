package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.grammar.GrammarRule;
import de.nschum.jbsandbox.grammar.JBGrammar;
import de.nschum.jbsandbox.parser.ParserTree;
import de.nschum.jbsandbox.source.SourceRange;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static de.nschum.jbsandbox.grammar.JBGrammar.*;

/**
 * Helper for ASTBuilder that parses expressions
 */
public class ASTExpressionBuilder extends ASTBaseBuilder {

    public ASTExpressionBuilder(JBGrammar grammar) {
        super(grammar);
    }

    Expression parseExpression(ParserTree parserTree, Scope scope) {
        final GrammarRule rule = parserTree.getRule();
        Expression result;

        JBGrammar grammar = getGrammar();
        if (rule == grammar.ruleParentheses) {
            result = parseRuleParentheses(parserTree, scope);
        } else if (rule == grammar.ruleReference) {
            result = parseRuleReference(parserTree, scope);
        } else if (rule == grammar.ruleRange) {
            result = parseRuleRange(parserTree, scope);
        } else if (rule == grammar.ruleNumber) {
            result = parseRuleNumber(parserTree);
        } else if (rule == grammar.ruleMap) {
            result = parseRuleMap(parserTree, scope);
        } else if (rule == grammar.ruleReduce) {
            result = parseRuleReduce(parserTree, scope);
        } else if (rule == grammar.ruleNegate) {
            result = parseRuleNegate(parserTree);
        } else {
            throw new IllegalStateException("Unknown rule");
        }

        final List<ParserTree> children = parserTree.getChildren();

        return parseExpressionRemainder(children.get(children.size() - 1), scope, result);
    }

    private Expression parseRuleParentheses(ParserTree parserTree, Scope scope) {
        assertRule(parserTree, PAREN_OPEN, EXPR, PAREN_CLOSE, EXPR_CONTINUED);

        ParserTree expressionTree = parserTree.getChild(1);

        return new ParenthesizedExpression(parseExpression(expressionTree, scope), parserTree.getLocation());
    }

    private Expression parseRuleReference(ParserTree parserTree, Scope scope) {
        assertRule(parserTree, IDENTIFIER, EXPR_CONTINUED);

        ParserTree identifierTree = parserTree.getChild(0);

        final String variableName = identifierTree.getContent().get();
        Optional<Variable> referencedVariable = scope.lookUp(variableName);
        if (!referencedVariable.isPresent()) {
            reportError(new UnresolvedVariableError("Unknown variable " + variableName, parserTree.getLocation()));
        }

        return new Reference(referencedVariable.orElse(new Variable(Type.UNDETERMINED, variableName)),
                parserTree.getLocation());
    }

    private Expression parseRuleRange(ParserTree parserTree, Scope scope) {
        assertRule(parserTree, BRACE_OPEN, EXPR, COMMA, EXPR, BRACE_CLOSE, EXPR_CONTINUED);

        ParserTree lowerBoundTree = parserTree.getChild(1);
        ParserTree upperBoundTree = parserTree.getChild(3);

        Expression lowerBound = parseExpression(lowerBoundTree, scope);
        Expression upperBound = parseExpression(upperBoundTree, scope);

        if (!lowerBound.getType().equals(Type.INT) || !upperBound.getType().equals(Type.INT)) {
            reportError(new TypeError("Range bounds must be integers (was {"
                    + lowerBound.getType() + "," + upperBound.getType() + "})",
                    parserTree.getLocation()));
        }

        return new IntRangeExpression(lowerBound, upperBound, parserTree.getLocation());
    }

    private Expression parseRuleNumber(ParserTree parserTree) {
        assertRule(parserTree, NUMBER, EXPR_CONTINUED);

        ParserTree numberTree = parserTree.getChild(0);

        return parseNumber(numberTree, 1);
    }

    private Expression parseRuleMap(ParserTree parserTree, Scope scope) {
        assertRule(parserTree, KEYWORD_MAP, PAREN_OPEN, EXPR, COMMA, IDENTIFIER, ARROW, EXPR, PAREN_CLOSE, EXPR_CONTINUED);

        ParserTree inputTree = parserTree.getChild(2);
        ParserTree parameterTree = parserTree.getChild(4);
        ParserTree expressionTree = parserTree.getChild(6);

        Expression input = parseExpression(inputTree, scope);
        if (!input.getType().isSequence()) {
            reportError(new TypeError("Map input must be a sequence (was " + input.getType() + ")",
                    parserTree.getLocation()));
        }
        Type innerType = input.getType().getInnerType();
        Variable parameter = new Variable(innerType, parameterTree.getContent().get());

        Expression lambda = parseExpression(expressionTree, scope.addVariable(parameter));

        final Lambda function = new Lambda(Arrays.asList(parameter), lambda, parserTree.getLocation());
        return new MapExpression(new SequenceType(lambda.getType()), input, function, parserTree.getLocation());
    }

    private Expression parseRuleReduce(ParserTree parserTree, Scope scope) {
        assertRule(parserTree, KEYWORD_REDUCE, PAREN_OPEN, EXPR, COMMA, EXPR, COMMA, IDENTIFIER, IDENTIFIER, ARROW, EXPR, PAREN_CLOSE, EXPR_CONTINUED);

        ParserTree inputTree = parserTree.getChild(2);
        ParserTree initialValueTree = parserTree.getChild(4);
        ParserTree parameterTree1 = parserTree.getChild(6);
        ParserTree parameterTree2 = parserTree.getChild(7);
        ParserTree expressionTree = parserTree.getChild(9);

        Expression input = parseExpression(inputTree, scope);
        Expression initialValue = parseExpression(initialValueTree, scope);
        Type type = promoteTypes(initialValue.getType(), input.getType().getInnerType(), parserTree.getLocation());
        if (!input.getType().isSequence()) {
            reportError(new TypeError("Reduce input must be a sequence (was " + input.getType() + ")",
                    parserTree.getLocation()));
        }
        Variable parameter1 = new Variable(type, parameterTree1.getContent().get());
        Variable parameter2 = new Variable(type, parameterTree2.getContent().get());

        Expression lambda = parseExpression(expressionTree, scope.addVariable(parameter1).addVariable(parameter2));
        if (!type.canBeAssignedFrom(lambda.getType())) {
            reportError(new TypeError("Lambda return type does not match sequence and initial value (was "
                    + lambda.getType() + ")",
                    parserTree.getLocation()));
        }

        final Lambda function = new Lambda(Arrays.asList(parameter1, parameter2), lambda, parserTree.getLocation());
        return new ReduceExpression(type, input, initialValue, function, parserTree.getLocation());
    }

    private Expression parseRuleNegate(ParserTree parserTree) {
        assertRule(parserTree, MINUS, NUMBER, EXPR_CONTINUED);

        ParserTree numberTree = parserTree.getChild(1);

        return parseNumber(numberTree, -1);
    }

    private Expression parseExpressionRemainder(ParserTree parserTree, Scope scope, Expression expression) {
        final GrammarRule rule = parserTree.getRule();

        JBGrammar grammar = getGrammar();
        if (rule == grammar.ruleOperator) {
            return parseRuleOperator(parserTree, scope, expression);
        } else if (rule == grammar.ruleNoOperator) {
            return parseRuleNoOperator(parserTree, expression);
        }

        throw new IllegalStateException("Unknown rule");
    }

    private Expression parseRuleOperator(ParserTree parserTree, Scope scope, Expression leftHandExpression) {
        // EXPR_CONTINUED is there, insert the operation expression above the expression
        assertRule(parserTree, OP, EXPR);

        ParserTree operationTree = parserTree.getChild(0);
        ParserTree rightHandExpressionTree = parserTree.getChild(1);

        Operation operation = parseOperation(operationTree);
        Expression rightHandExpression = parseExpression(rightHandExpressionTree, scope);

        Type leftHandType = leftHandExpression.getType();
        Type rightHandType = rightHandExpression.getType();
        Type type = promoteTypes(leftHandType, rightHandType, operationTree.getLocation());
        if (!type.canPerformArithmetic()) {
            reportError(new TypeError("Operation arguments must be integers (was "
                    + leftHandType + " and " + rightHandType + ")",
                    parserTree.getLocation()));
        }

        OperationExpression result = new OperationExpression(type, leftHandExpression, rightHandExpression, operation);
        return new OperatorPrecedenceSyntaxTreeRewriter().reorderOperations(result);
    }

    private Expression parseRuleNoOperator(ParserTree parserTree, Expression expression) {
        // No remainder, just return the original expression.
        assertRule(parserTree, EPSILON);
        return expression;
    }

    private Type promoteTypes(Type type1, Type type2, SourceRange location) {
        if (type1.equals(Type.UNDETERMINED) || type2.equals(Type.UNDETERMINED)) {
            return Type.UNDETERMINED;
        } else if (type1.canBeAssignedFrom(type2)) {
            return type1;
        } else if (type2.canBeAssignedFrom(type1)) {
            return type2;
        } else {
            reportError(new TypeError("Incompatible types " + type1 + " and " + type2, location));
            return Type.UNDETERMINED;
        }
    }

    private Operation parseOperation(ParserTree parserTree) {
        final GrammarRule rule = parserTree.getRule();

        JBGrammar grammar = getGrammar();
        if (rule == grammar.rulePlus) {
            assertRule(parserTree, PLUS);
            return Operation.PLUS;
        } else if (rule == grammar.ruleMinus) {
            assertRule(parserTree, MINUS);
            return Operation.MINUS;
        } else if (rule == grammar.ruleMultiply) {
            assertRule(parserTree, STAR);
            return Operation.MULTIPLY;
        } else if (rule == grammar.ruleDivide) {
            assertRule(parserTree, SLASH);
            return Operation.DIVIDE;
        } else if (rule == grammar.ruleExp) {
            assertRule(parserTree, HAT);
            return Operation.EXP;
        }

        throw new IllegalStateException("Unknown rule");
    }

    private Expression parseNumber(ParserTree numberTree, int factor) {
        Expression result;
        final String content = numberTree.getContent().get();
        if (content.contains(".")) {
            result = new FloatLiteral(Double.parseDouble(content) * factor, numberTree.getLocation());
        } else {
            result = new IntLiteral(Integer.parseInt(content) * factor, numberTree.getLocation());
        }
        return result;
    }
}
