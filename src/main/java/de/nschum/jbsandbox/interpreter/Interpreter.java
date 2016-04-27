package de.nschum.jbsandbox.interpreter;

import de.nschum.jbsandbox.ast.*;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.CompletionException;

/**
 * Executes a program defined by its abstract syntax tree
 */
public class Interpreter {

    private final Writer outputWriter;
    private final ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

    private volatile boolean cancelled;

    public Interpreter(Writer outputWriter) {
        this.outputWriter = outputWriter;
    }

    public void cancel() {
        cancelled = true;
        expressionEvaluator.cancel();
    }

    private void checkIfCancelled() {
        if (cancelled) {
            throw new InterpreterCancelledException();
        }
    }

    public void execute(Program program)
            throws IOException, InterpreterRuntimeException, InterpreterCancelledException {
        try {
            State state = new State();
            for (Statement statement : program.getStatements()) {
                execute(statement, state);
            }
            outputWriter.flush();
        } catch (CompletionException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            } else {
                throw e;
            }
        }
    }

    private void execute(Statement statement, State state) throws IOException {
        checkIfCancelled();
        if (statement instanceof PrintStatement) {
            executePrintStatement((PrintStatement) statement);
        } else if (statement instanceof OutStatement) {
            executeOutStatement((OutStatement) statement, state);
        } else if (statement instanceof Declaration) {
            executeDeclaration((Declaration) statement, state);
        }
    }

    private void executePrintStatement(PrintStatement statement) throws IOException {
        outputWriter.write(statement.getString());
    }

    private void executeOutStatement(OutStatement statement, State state) throws IOException {
        outputWriter.write(evaluateExpression(statement.getExpression(), state).toString());
    }

    private void executeDeclaration(Declaration statement, State state) {
        state.store(statement.getVariable(), evaluateExpression(statement.getExpression(), state));
    }

    private Value evaluateExpression(Expression expression, State state) {
        return expressionEvaluator.evaluateExpression(expression, state);
    }
}
