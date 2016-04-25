package de.nschum.jbsandbox.ui;

import de.nschum.jbsandbox.ast.Program;
import de.nschum.jbsandbox.interpreter.Interpreter;
import de.nschum.jbsandbox.interpreter.InterpreterCancelledException;
import de.nschum.jbsandbox.interpreter.InterpreterRuntimeException;
import de.nschum.jbsandbox.source.SourceFile;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

/**
 * Executes a program on a background thread and notifies listeners of the result
 */
public class BackgroundInterpreter {

    private List<Consumer<InterpreterResult>> listeners = new ArrayList<>();
    private Optional<Interpreter> previousInterpreter = Optional.empty();
    private int runCount = 0;

    void run(SourceFile sourceFile, Program program) {
        // main thread:
        final int currentRun = ++runCount;
        cancel();

        StringWriter stringWriter = new StringWriter();
        Interpreter interpreter = new Interpreter(new BufferedWriter(stringWriter));

        previousInterpreter = Optional.of(interpreter);

        CompletableFuture<InterpreterResult> future = CompletableFuture.supplyAsync(() -> {
            try {
                interpreter.execute(program);

                return new InterpreterResult(sourceFile, emptyList(), stringWriter.toString());
            } catch (IOException e) {
                return new InterpreterResult(sourceFile, emptyList(), "IO error");
            } catch (InterpreterRuntimeException e) {
                EditorError error = new EditorError(e.getMessage(), e.getLocation());
                return new InterpreterResult(sourceFile, asList(error), stringWriter.toString());
            } catch (InterpreterCancelledException e) {
                throw e;
            }
        });
        future.thenAcceptAsync(interpreterResult -> {
            // main thread:
            if (currentRun == runCount) {
                // No new parse, no new edit, we can notify
                for (Consumer<InterpreterResult> listener : listeners) {
                    listener.accept(interpreterResult);
                }
            }
        }, SwingUtilities::invokeLater);
    }

    void cancel() {
        previousInterpreter.ifPresent(Interpreter::cancel);
    }

    public void addResultListener(Consumer<InterpreterResult> consumer) {
        listeners.add(consumer);
    }
}
