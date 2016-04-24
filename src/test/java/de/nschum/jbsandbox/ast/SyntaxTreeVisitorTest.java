package de.nschum.jbsandbox.ast;

import de.nschum.jbsandbox.source.SourceRange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static de.nschum.jbsandbox.grammar.GrammarToken.terminal;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class SyntaxTreeVisitorTest {

    @Mock(name = "sourceRangeMock", answer = Answers.RETURNS_DEEP_STUBS)
    private SourceRange sourceRangeMock;

    @Mock(name = "terminalMock")
    private Terminal terminalMock;

    @Mock(name = "visitor")
    private Consumer<SyntaxTree> visitor;

    private List<Terminal> terminalMocks;

    @Before
    public void setUp() throws Exception {
        terminalMocks = Arrays.asList(terminalMock);
    }

    @Test
    public void shouldVisitProgramRecursively() throws Exception {
        // given
        Statement statement1 = mock(Statement.class);
        Statement statement2 = mock(Statement.class);
        Statement statement3 = mock(Statement.class);
        Program program = new Program(Arrays.asList(statement1, statement2, statement3), sourceRangeMock);

        // when
        program.visit(visitor);

        // then
        verify(visitor).accept(program);
        verify(statement1).visit(visitor);
        verify(statement2).visit(visitor);
        verify(statement3).visit(visitor);
    }

    @Test
    public void shouldVisitDeclarationRecursively() throws Exception {
        // given
        Variable variable = mock(Variable.class);
        Expression expression = mock(Expression.class);
        Declaration declaration = new Declaration(variable, expression, terminalMocks, sourceRangeMock);

        // when
        declaration.visit(visitor);

        // then
        verify(visitor).accept(declaration);
        verify(visitor).accept(terminalMock);
        verify(expression).visit(visitor);
    }

    @Test
    public void shouldVisitOutStatementRecursively() throws Exception {
        // given
        Expression expression = mock(Expression.class);
        OutStatement outStatement = new OutStatement(expression, terminalMocks, sourceRangeMock);

        // when
        outStatement.visit(visitor);

        // then
        verify(visitor).accept(outStatement);
        verify(visitor).accept(terminalMock);
        verify(expression).visit(visitor);
    }

    @Test
    public void shouldVisitPrintStatement() throws Exception {
        // given
        PrintStatement outStatement = new PrintStatement("foo", terminalMocks, sourceRangeMock);

        // when
        outStatement.visit(visitor);

        // then
        verify(visitor).accept(outStatement);
        verify(visitor).accept(terminalMock);
    }

    @Test
    public void shouldVisitIntRangeRecursively() throws Exception {
        // given
        Expression expr1 = mock(Expression.class);
        Expression expr2 = mock(Expression.class);
        IntRangeExpression intRangeExpression = new IntRangeExpression(expr1, expr2, terminalMocks, sourceRangeMock);

        // when
        intRangeExpression.visit(visitor);

        // then
        verify(visitor).accept(intRangeExpression);
        verify(visitor).accept(terminalMock);
        verify(expr1).visit(visitor);
        verify(expr2).visit(visitor);
    }

    @Test
    public void shouldVisitFloatLiteral() throws Exception {
        // given
        FloatLiteral floatLiteral = new FloatLiteral(42.0, sourceRangeMock);

        // when
        floatLiteral.visit(visitor);

        // then
        verify(visitor).accept(floatLiteral);
    }

    @Test
    public void shouldVisitIntLiteral() throws Exception {
        // given
        IntLiteral intLiteral = new IntLiteral(42, sourceRangeMock);

        // when
        intLiteral.visit(visitor);

        // then
        verify(visitor).accept(intLiteral);
    }

    @Test
    public void shouldVisitOperationExpressionRecursively() throws Exception {
        // given
        Expression expr1 = mock(Expression.class);
        given(expr1.getLocation()).willReturn(sourceRangeMock);
        Expression expr2 = mock(Expression.class);
        given(expr2.getLocation()).willReturn(sourceRangeMock);
        OperationExpression operationExpression =
                new OperationExpression(Type.INT, expr1, expr2, Operation.PLUS, terminalMocks);

        // when
        operationExpression.visit(visitor);

        // then
        verify(visitor).accept(operationExpression);
        verify(visitor).accept(terminalMock);
        verify(expr1).visit(visitor);
        verify(expr2).visit(visitor);
    }

    @Test
    public void shouldVisitParenthesizedExpressionRecursively() throws Exception {
        // given
        Expression expr = mock(Expression.class);
        ParenthesizedExpression parenthesizedExpression =
                new ParenthesizedExpression(expr, terminalMocks, sourceRangeMock);

        // when
        parenthesizedExpression.visit(visitor);

        // then
        verify(visitor).accept(parenthesizedExpression);
        verify(visitor).accept(terminalMock);
        verify(expr).visit(visitor);
    }

    @Test
    public void shouldVisitReference() throws Exception {
        // given
        Variable variable = mock(Variable.class);
        Reference reference = new Reference(variable, terminalMocks, sourceRangeMock);

        // when
        reference.visit(visitor);

        // then
        verify(visitor).accept(reference);
        verify(visitor).accept(terminalMock);
    }

    @Test
    public void shouldVisitMapExpressionRecursively() throws Exception {
        // given
        Expression input = mock(Expression.class);
        Lambda lambda = mock(Lambda.class);
        MapExpression mapExpression = new MapExpression(Type.INT, input, lambda, terminalMocks, sourceRangeMock);

        // when
        mapExpression.visit(visitor);

        // then
        verify(visitor).accept(mapExpression);
        verify(visitor).accept(terminalMock);
        verify(input).visit(visitor);
        verify(lambda).visit(visitor);
    }

    @Test
    public void shouldVisitReduceExpressionRecursively() throws Exception {
        // given
        Expression input = mock(Expression.class);
        Expression initialValue = mock(Expression.class);
        Lambda lambda = mock(Lambda.class);
        ReduceExpression mapExpression =
                new ReduceExpression(Type.INT, input, initialValue, lambda, terminalMocks, sourceRangeMock);

        // when
        mapExpression.visit(visitor);

        // then
        verify(visitor).accept(mapExpression);
        verify(visitor).accept(terminalMock);
        verify(input).visit(visitor);
        verify(initialValue).visit(visitor);
        verify(lambda).visit(visitor);
    }

    @Test
    public void shouldVisitLambdaRecursively() throws Exception {
        // given
        Expression expr = mock(Expression.class);
        Lambda lambda = new Lambda(Collections.emptyList(), expr, terminalMocks, sourceRangeMock);

        // when
        lambda.visit(visitor);

        // then
        verify(visitor).accept(lambda);
        verify(visitor).accept(terminalMock);
        verify(expr).visit(visitor);
    }

    @Test
    public void shouldVisitTerminal() throws Exception {
        // given
        Terminal terminal = new Terminal(terminal("foo"), sourceRangeMock);

        // when
        terminal.visit(visitor);

        // then
        verify(visitor).accept(terminal);
    }
}
