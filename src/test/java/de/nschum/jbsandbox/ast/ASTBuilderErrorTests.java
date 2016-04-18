package de.nschum.jbsandbox.ast;

import org.junit.Test;

import static de.nschum.jbsandbox.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

public class ASTBuilderErrorTests extends ASTBuilderBaseTests {

    @Test
    public void shouldNotAllowFloatAsLowerBoundInRange() throws Exception {
        parseExpression("{1.0, 2}");

        assertThat(astBuilder.getErrors(), contains(instanceOf(TypeError.class)));
    }

    @Test
    public void shouldNotAllowFloatAsUpperBoundInRange() throws Exception {
        parseExpression("{1, 2.0}");

        assertThat(astBuilder.getErrors(), contains(instanceOf(TypeError.class)));
    }

    @Test
    public void shouldNotAllowSequenceOnRightHandSideOfOperation() throws Exception {
        parseExpression("1 + {1, 2}");

        assertThat(astBuilder.getErrors(), contains(instanceOf(TypeError.class)));
    }

    @Test
    public void shouldNotAllowSequenceOnLeftHandSideOfOperation() throws Exception {
        parseExpression("{1, 2} + 1");

        assertThat(astBuilder.getErrors(), contains(instanceOf(TypeError.class)));
    }

    @Test
    public void shouldNotAllowSequenceOnBothSidesOfOperation() throws Exception {
        parseExpression("{1, 2} + {3, 4}");

        assertThat(astBuilder.getErrors(), contains(instanceOf(TypeError.class)));
    }

    @Test
    public void shouldNotAllowMappingScalar() throws Exception {
        parseExpression("map(1, i -> i)");

        assertThat(astBuilder.getErrors(), contains(instanceOf(TypeError.class)));
    }

    @Test
    public void shouldNotAllowInitialValueThatIsNotAssignableFromReduceResult() throws Exception {
        parseExpression("reduce({1, 5}, 0, x y -> x + y + 0.5)");

        assertThat(astBuilder.getErrors(), contains(instanceOf(TypeError.class)));
    }

    @Test
    public void shouldNotAllowReducingScalar() throws Exception {
        parseExpression("reduce(1, 0, x y -> x + y)");

        assertThat(astBuilder.getErrors(), contains(instanceOf(TypeError.class)));
    }

    @Test
    public void shouldNotAllowReferencingUndeclaredVariables() throws Exception {
        parseExpression("x");

        assertThat(astBuilder.getErrors(), contains(instanceOf(UnresolvedVariableError.class)));
    }

}
