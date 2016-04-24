package de.nschum.jbsandbox.ast;

import org.junit.After;
import org.junit.Test;

import static de.nschum.jbsandbox.Matchers.contains;
import static de.nschum.jbsandbox.grammar.JBGrammar.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ASTBuilderExpressionTests extends ASTBuilderBaseTests {

    @After
    public void tearDown() throws Exception {
        assertBuilderHasNoErrors();
    }

    @Test
    public void shouldReturnInts() throws Exception {
        Expression expression = parseExpression("42");

        assertThat(expression, instanceOf(IntLiteral.class));
        assertThat(expression, hasProperty("content", equalTo(42)));
        assertThat(expression, hasProperty("terminals", empty()));
    }

    @Test
    public void shouldReturnFloats() throws Exception {
        Expression expression = parseExpression("42.0");

        assertThat(expression, instanceOf(FloatLiteral.class));
        assertThat(expression, hasProperty("content", equalTo(42.0)));
        assertThat(expression, hasProperty("terminals", empty()));
    }

    @Test
    public void shouldReturnNegativeInts() throws Exception {
        Expression expression = parseExpression("-42");

        assertThat(expression, instanceOf(IntLiteral.class));
        assertThat(expression, hasProperty("content", equalTo(-42)));
        assertThat(expression, hasProperty("terminals", empty()));
    }

    @Test
    public void shouldReturnNegativeFloats() throws Exception {
        Expression expression = parseExpression("-42.0");

        assertThat(expression, instanceOf(FloatLiteral.class));
        assertThat(expression, hasProperty("content", equalTo(-42.0)));
        assertThat(expression, hasProperty("terminals", empty()));
    }

    @Test
    public void shouldReturnInterval() throws Exception {
        Expression expression = parseExpression("{4, 10}");

        assertThat(expression, instanceOf(IntRangeExpression.class));
        assertThat(expression, hasProperty("type", equalTo(Type.INT_SEQUENCE)));
        assertThat(expression, hasProperty("lowerBound", hasProperty("content", equalTo(4))));
        assertThat(expression, hasProperty("upperBound", hasProperty("content", equalTo(10))));
        assertThat(expression, hasProperty("terminals", contains(
                terminal(BRACE_OPEN, 4, 5),
                terminal(COMMA, 6, 7),
                terminal(BRACE_CLOSE, 10, 11))));
    }

    @Test
    public void shouldReturnOperation() throws Exception {
        Expression expression = parseExpression("1 + 2");

        assertThat(expression, instanceOf(OperationExpression.class));
        assertThat(expression, hasProperty("leftHandSide", hasProperty("content", equalTo(1))));
        assertThat(expression, hasProperty("rightHandSide", hasProperty("content", equalTo(2))));
        assertThat(expression, hasProperty("terminals", contains(terminal(PLUS, 6, 7))));
    }

    @Test
    public void shouldReturnPlusOperation() throws Exception {
        Expression expression = parseExpression("1 + 2");

        assertThat(expression, instanceOf(OperationExpression.class));
        assertThat(expression, hasProperty("operation", equalTo(Operation.PLUS)));
        assertThat(expression, hasProperty("terminals", contains(terminal(PLUS, 6, 7))));
    }

    @Test
    public void shouldReturnMinusOperation() throws Exception {
        Expression expression = parseExpression("1 - 2");

        assertThat(expression, instanceOf(OperationExpression.class));
        assertThat(expression, hasProperty("operation", equalTo(Operation.MINUS)));
        assertThat(expression, hasProperty("terminals", contains(terminal(MINUS, 6, 7))));
    }

    @Test
    public void shouldReturnMultiplyOperation() throws Exception {
        Expression expression = parseExpression("1 * 2");

        assertThat(expression, instanceOf(OperationExpression.class));
        assertThat(expression, hasProperty("operation", equalTo(Operation.MULTIPLY)));
        assertThat(expression, hasProperty("terminals", contains(terminal(STAR, 6, 7))));
    }

    @Test
    public void shouldReturnDivideOperation() throws Exception {
        Expression expression = parseExpression("1 / 2");

        assertThat(expression, instanceOf(OperationExpression.class));
        assertThat(expression, hasProperty("operation", equalTo(Operation.DIVIDE)));
        assertThat(expression, hasProperty("terminals", contains(terminal(SLASH, 6, 7))));
    }

    @Test
    public void shouldReturnExpOperation() throws Exception {
        Expression expression = parseExpression("1^2");

        assertThat(expression, instanceOf(OperationExpression.class));
        assertThat(expression, hasProperty("operation", equalTo(Operation.EXP)));
        assertThat(expression, hasProperty("terminals", contains(terminal(HAT, 5, 6))));
    }

    @Test
    public void shouldReturnIntOperation() throws Exception {
        Expression expression = parseExpression("1 + 2");

        assertThat(expression, instanceOf(OperationExpression.class));
        assertThat(expression, hasProperty("type", equalTo(Type.INT)));
    }

    @Test
    public void shouldReturnFloatOperation() throws Exception {
        Expression expression = parseExpression("1.0 + 2.0");

        assertThat(expression, instanceOf(OperationExpression.class));
        assertThat(expression, hasProperty("type", equalTo(Type.FLOAT)));
    }

    @Test
    public void shouldIgnoreRedundantParentheses() throws Exception {
        Expression expression = parseExpression("(42.0)");

        assertThat(expression, instanceOf(ParenthesizedExpression.class));
        assertThat(expression, hasProperty("expression", allOf(
                instanceOf(FloatLiteral.class),
                hasProperty("type", equalTo(Type.FLOAT)),
                hasProperty("content", equalTo(42.0)))));
        assertThat(expression, hasProperty("terminals", contains(
                terminal(PAREN_OPEN, 4, 5),
                terminal(PAREN_CLOSE, 9, 10))));
    }

    @Test
    public void shouldReturnMapExpression() throws Exception {
        Expression expression = parseExpression("map({1, 5}, i -> i)");

        assertThat(expression, instanceOf(MapExpression.class));
        assertThat(expression, hasProperty("type", equalTo(Type.INT_SEQUENCE)));
        assertThat(expression, hasProperty("input", instanceOf(IntRangeExpression.class)));
        assertThat(expression, hasProperty("terminals", contains(
                terminal(KEYWORD_MAP, 4, 7),
                terminal(PAREN_OPEN, 7, 8),
                terminal(COMMA, 14, 15),
                terminal(PAREN_CLOSE, 22, 23))));

        if (expression instanceof MapExpression) {
            final Lambda function = ((MapExpression) expression).getFunction();
            assertThat(function, hasProperty("terminals", contains(
                    terminal(IDENTIFIER, 16, 17),
                    terminal(ARROW, 18, 20)
            )));

            final Variable parameter1 = function.getParameters().get(0);
            assertThat(parameter1.getName(), equalTo("i"));
            assertThat(parameter1.getType(), equalTo(Type.INT));

            final Expression functionExpression = function.getExpression();
            assertThat(functionExpression, instanceOf(Reference.class));
            assertThat(functionExpression, hasProperty("variable", is(parameter1)));
            assertThat(functionExpression, hasProperty("type", equalTo(Type.INT)));
        }
    }

    @Test
    public void shouldReturnReduceExpression() throws Exception {
        Expression expression = parseExpression("reduce({1, 5}, 0, x y -> x + y)");

        assertThat(expression, instanceOf(ReduceExpression.class));
        assertThat(expression, hasProperty("type", equalTo(Type.INT)));
        assertThat(expression, hasProperty("input", instanceOf(IntRangeExpression.class)));
        assertThat(expression, hasProperty("initialValue", hasProperty("content", equalTo(0))));
        assertThat(expression, hasProperty("terminals", contains(
                terminal(KEYWORD_REDUCE, 4, 10),
                terminal(PAREN_OPEN, 10, 11),
                terminal(COMMA, 17, 18),
                terminal(COMMA, 20, 21),
                terminal(PAREN_CLOSE, 34, 35))));

        if (expression instanceof ReduceExpression) {
            final Lambda function = ((ReduceExpression) expression).getFunction();
            assertThat(function, hasProperty("terminals", contains(
                    terminal(IDENTIFIER, 22, 23),
                    terminal(IDENTIFIER, 24, 25),
                    terminal(ARROW, 26, 28)
            )));

            final Variable parameter1 = function.getParameters().get(0);
            assertThat(parameter1.getName(), equalTo("x"));
            assertThat(parameter1.getType(), equalTo(Type.INT));

            final Variable parameter2 = function.getParameters().get(1);
            assertThat(parameter2.getName(), equalTo("y"));
            assertThat(parameter2.getType(), equalTo(Type.INT));

            assertThat(function.getExpression(), instanceOf(OperationExpression.class));
        }
    }
}
