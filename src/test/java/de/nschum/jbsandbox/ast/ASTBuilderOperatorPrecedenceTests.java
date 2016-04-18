package de.nschum.jbsandbox.ast;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Test;

import static de.nschum.jbsandbox.ast.Operation.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ASTBuilderOperatorPrecedenceTests extends ASTBuilderBaseTests {

    // matchers

    private Matcher<Object> operation(final Operation plus, final int lhs, final int rhs) {
        return allOf(
                instanceOf(OperationExpression.class),
                hasProperty("operation", equalTo(plus)),
                hasProperty("leftHandSide", hasProperty("content", equalTo(lhs))),
                hasProperty("rightHandSide", hasProperty("content", equalTo(rhs))));
    }

    private Matcher<Object> operation(final Operation plus, final Matcher<Object> lhs, final int rhs) {
        return allOf(
                instanceOf(OperationExpression.class),
                hasProperty("operation", equalTo(plus)),
                hasProperty("leftHandSide", lhs),
                hasProperty("rightHandSide", hasProperty("content", equalTo(rhs))));
    }

    private Matcher<Object> operation(final Operation plus, final int lhs, final Matcher<Object> rhs) {
        return allOf(
                instanceOf(OperationExpression.class),
                hasProperty("operation", equalTo(plus)),
                hasProperty("leftHandSide", hasProperty("content", equalTo(lhs))),
                hasProperty("rightHandSide", rhs));
    }

    private Matcher<Object> parenthized(Matcher<Object> matcher) {
        return allOf(
                instanceOf(ParenthesizedExpression.class),
                hasProperty("expression", matcher));
    }

    // tests

    @After
    public void tearDown() throws Exception {
        assertBuilderHasNoErrors();
    }

    @Test
    public void plusAndMinusShouldHaveSamePrecedence() throws Exception {
        Expression expression = parseExpression("42 - 21 + 7 - 1");

        assertThat(expression, operation(MINUS, operation(PLUS, operation(MINUS, 42, 21), 7), 1));
    }

    @Test
    public void multiplyAndDivideShouldHaveSamePrecedence() throws Exception {
        Expression expression = parseExpression("42 * 21 / 7 * 1");

        assertThat(expression, operation(MULTIPLY, operation(DIVIDE, operation(MULTIPLY, 42, 21), 7), 1));
    }

    @Test
    public void multiplyShouldHaveHigherPrecedenceThanPlusAndMinus() throws Exception {
        Expression expression = parseExpression("42 + 21 * 7 - 1");

        assertThat(expression, operation(MINUS, operation(PLUS, 42, operation(MULTIPLY, 21, 7)), 1));
    }

    @Test
    public void divideShouldHaveHigherPrecedenceThanPlusAndMinus() throws Exception {
        Expression expression = parseExpression("42 + 21 / 7 - 1");

        assertThat(expression, operation(MINUS, operation(PLUS, 42, operation(DIVIDE, 21, 7)), 1));
    }

    @Test
    public void expShouldHaveHigherPrecedenceThanPlusAndMinus() throws Exception {
        Expression expression = parseExpression("42 + 21 ^ 7 - 1");

        assertThat(expression, operation(MINUS, operation(PLUS, 42, operation(EXP, 21, 7)), 1));
    }

    @Test
    public void expShouldHaveHigherPrecedenceThanMultiplyAndDivide() throws Exception {
        Expression expression = parseExpression("42 * 21 ^ 7 / 1");

        assertThat(expression, operation(DIVIDE, operation(MULTIPLY, 42, operation(EXP, 21, 7)), 1));
    }

    @Test
    public void parenthesesShouldTakePrecedence() throws Exception {
        Expression expression = parseExpression("(42 + 21) * 7 - 1");

        assertThat(expression, operation(MINUS, operation(MULTIPLY, parenthized(operation(PLUS, 42, 21)), 7), 1));
    }

}
