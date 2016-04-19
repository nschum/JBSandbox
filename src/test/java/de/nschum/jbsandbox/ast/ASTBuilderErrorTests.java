package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.SourceRange;
import org.hamcrest.Matcher;
import org.junit.Test;

import static de.nschum.jbsandbox.Matchers.contains;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ASTBuilderErrorTests extends ASTBuilderBaseTests {

    private Matcher<Object> error(Class<? extends ASTError> clazz,
                                  int startLine, int startColumn, int endLine, int endColumn) {
        return allOf(
                instanceOf(clazz),
                hasProperty("location", equalTo(new SourceRange(startLine, startColumn, endLine, endColumn))));
    }

    // tests

    @Test
    public void shouldNotAllowFloatAsLowerBoundInRange() throws Exception {
        parseExpression("{1.0, 2}");

        assertThat(astBuilder.getErrors(), contains(error(TypeError.class, 0, 4, 0, 12)));
    }

    @Test
    public void shouldNotAllowFloatAsUpperBoundInRange() throws Exception {
        parseExpression("{1, 2.0}");

        assertThat(astBuilder.getErrors(), contains(error(TypeError.class, 0, 4, 0, 12)));
    }

    @Test
    public void shouldNotAllowSequenceOnRightHandSideOfOperation() throws Exception {
        parseExpression("1 + {1, 2}");

        assertThat(astBuilder.getErrors(), contains(error(TypeError.class, 0, 6, 0, 7)));
    }

    @Test
    public void shouldNotAllowSequenceOnLeftHandSideOfOperation() throws Exception {
        parseExpression("{1, 2} + 1");

        assertThat(astBuilder.getErrors(), contains(error(TypeError.class, 0, 11, 0, 12)));
    }

    @Test
    public void shouldNotAllowSequenceOnBothSidesOfOperation() throws Exception {
        parseExpression("{1, 2} + {3, 4}");

        assertThat(astBuilder.getErrors(), contains(error(TypeError.class, 0, 11, 0, 19)));
    }

    @Test
    public void shouldNotAllowMappingScalar() throws Exception {
        parseExpression("map(1, i -> i)");

        assertThat(astBuilder.getErrors(), contains(error(TypeError.class, 0, 4, 0, 18)));
    }

    @Test
    public void shouldNotAllowInitialValueThatIsNotAssignableFromReduceResult() throws Exception {
        parseExpression("reduce({1, 5}, 0, x y -> x + y + 0.5)");

        assertThat(astBuilder.getErrors(), contains(error(TypeError.class, 0, 4, 0, 41)));
    }

    @Test
    public void shouldNotAllowReducingScalar() throws Exception {
        parseExpression("reduce(1, 0, x y -> x + y)");

        assertThat(astBuilder.getErrors(), contains(error(TypeError.class, 0, 4, 0, 30)));
    }

    @Test
    public void shouldNotAllowReferencingUndeclaredVariables() throws Exception {
        parseExpression("x");

        assertThat(astBuilder.getErrors(), contains(error(UnresolvedVariableError.class, 0, 4, 0, 5)));
    }

}
