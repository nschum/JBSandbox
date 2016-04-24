package de.nschum.jbsandbox.ast;

import org.junit.After;
import org.junit.Test;

import static de.nschum.jbsandbox.Matchers.contains;
import static de.nschum.jbsandbox.grammar.JBGrammar.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ASTBuilderStatementTests extends ASTBuilderBaseTests {

    @After
    public void tearDown() throws Exception {
        assertBuilderHasNoErrors();
    }

    @Test
    public void shouldReturnStatements() throws Exception {
        Program program = parse("print \"foo\" print \"bar\"");

        assertThat(program.getStatements(), contains(
                allOf(instanceOf(PrintStatement.class), hasProperty("string", equalTo("foo"))),
                allOf(instanceOf(PrintStatement.class), hasProperty("string", equalTo("bar")))));
    }

    @Test
    public void shouldReturnPrintStatements() throws Exception {
        Statement statement = parse("print \"foo\"").getStatements().get(0);

        assertThat(statement, instanceOf(PrintStatement.class));
        assertThat(statement, hasProperty("string", equalTo("foo")));
        assertThat(statement, hasProperty("terminals", contains(
                terminal(KEYWORD_PRINT, 0, 5),
                terminal(STRING, 6, 11))));
    }

    @Test
    public void shouldReturnDeclarations() throws Exception {
        Statement statement = parse("var foo = 42").getStatements().get(0);

        assertThat(statement, instanceOf(Declaration.class));
        assertThat(statement, hasProperty("variable", allOf(
                instanceOf(Variable.class),
                hasProperty("name", equalTo("foo")),
                hasProperty("type", equalTo(Type.INT)))));
        assertThat(statement, hasProperty("expression", allOf(
                instanceOf(IntLiteral.class),
                hasProperty("type", equalTo(Type.INT)),
                hasProperty("content", equalTo(42)))));
        assertThat(statement, hasProperty("terminals", contains(
                terminal(KEYWORD_VAR, 0, 3),
                terminal(IDENTIFIER, 4, 7),
                terminal(EQUALS, 8, 9))));
    }

    @Test
    public void shouldReturnOutStatements() throws Exception {
        Statement statement = parse("out 42").getStatements().get(0);

        assertThat(statement, instanceOf(OutStatement.class));
        assertThat(statement, hasProperty("expression", allOf(
                instanceOf(IntLiteral.class),
                hasProperty("type", equalTo(Type.INT)),
                hasProperty("content", equalTo(42)))));
        assertThat(statement, hasProperty("terminals", contains(terminal(KEYWORD_OUT, 0, 3))));
    }

    @Test
    public void shouldReturnReferenceStatements() throws Exception {
        Program program = parse("var foo = 42 out foo");
        Declaration declaration = (Declaration) program.getStatements().get(0);
        OutStatement outStatement = (OutStatement) program.getStatements().get(1);
        Expression expression = outStatement.getExpression();

        assertThat(expression, instanceOf(Reference.class));
        assertThat(expression, hasProperty("variable", is(declaration.getVariable())));
        assertThat(expression, hasProperty("type", equalTo(Type.INT)));
    }
}
