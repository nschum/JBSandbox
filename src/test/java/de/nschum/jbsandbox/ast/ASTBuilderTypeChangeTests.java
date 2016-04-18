package de.nschum.jbsandbox.ast;

import org.junit.After;
import org.junit.Test;

import static de.nschum.jbsandbox.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;

public class ASTBuilderTypeChangeTests extends ASTBuilderBaseTests {

    @After
    public void tearDown() throws Exception {
        assertBuilderHasNoErrors();
    }

    @Test
    public void shouldPromoteIntOnLeftHandSideOfOperationWithFloat() throws Exception {
        Expression expression = parseExpression("1 + 2.0");

        assertThat(expression, hasProperty("type", equalTo(Type.FLOAT)));
    }

    @Test
    public void shouldPromoteIntOnRightHandSideOfOperationWithFloat() throws Exception {
        Expression expression = parseExpression("1.0 + 2");

        assertThat(expression, hasProperty("type", equalTo(Type.FLOAT)));
    }

    @Test
    public void shouldPromoteCorrectSubexpressionInRewrittenTrees() throws Exception {
        Expression expression = parseExpression("1.0 * 1 + 1");

        assertThat(expression, hasProperty("type", equalTo(Type.FLOAT)));
    }

    @Test
    public void shouldUndoPromotionInFormerRootInRewrittenTrees() throws Exception {
        Expression expression = parseExpression("1 * 1 + 1.0");

        assertThat(expression, hasProperty("leftHandSide", hasProperty("type", equalTo(Type.INT))));
    }

    @Test
    public void shouldBeAbleToMapToFloatSequence() throws Exception {
        final Expression expression = parseExpression("map({1, 5}, i -> i * 2.0)");

        assertThat(expression, hasProperty("type", equalTo(Type.FLOAT.asSequence())));
    }

    @Test
    public void shouldBeAbleToMapToSequenceSequence() throws Exception {
        final Expression expression = parseExpression("map({1, 5}, i -> {1, i})");

        assertThat(expression, hasProperty("type", equalTo(Type.INT.asSequence().asSequence())));
    }

    @Test
    public void shouldPromoteSequenceWhenInitialValueInReduceIsFloat() throws Exception {
        final Expression expression = parseExpression("reduce({1, 5}, 0.0, x y -> x + y)");

        assertThat(expression, hasProperty("type", equalTo(Type.FLOAT)));
        assertThat(expression, hasProperty("function", hasProperty("parameters", contains(
                hasProperty("type", equalTo(Type.FLOAT)),
                hasProperty("type", equalTo(Type.FLOAT))))));
    }

    @Test
    public void shouldPromoteInitialValueInReduceWhenSequenceIsFloat() throws Exception {
        final Expression expression = parseExpression("reduce(map({1, 5}, i -> i + 0.0), 0, x y -> x + y)");

        assertThat(expression, hasProperty("type", equalTo(Type.FLOAT)));
        assertThat(expression, hasProperty("function", hasProperty("parameters", contains(
                hasProperty("type", equalTo(Type.FLOAT)),
                hasProperty("type", equalTo(Type.FLOAT))))));
    }
}
