package de.nschum.jbsandbox.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A sequence of values
 * <p/>
 * All sequences are immutable.
 */
public interface Sequence {

    int size();

    default LazySequence map(Function<? super Value, ? extends Value> function) {
        return new LazySequence(this, function);
    }

    default Value reduce(Value initialValue, BinaryOperator<Value> function) {
        int size = size();
        int cores = Runtime.getRuntime().availableProcessors();
        int chunkSize = Math.max(1, size / cores);

        List<CompletableFuture<Value>> all = new ArrayList<>();
        for (int i = 0; i < size; i += chunkSize) {
            long limit = Math.min(chunkSize, size - i);
            final Stream<Value> substream = stream().skip(i).limit(limit);
            all.add(CompletableFuture.supplyAsync(() -> substream.reduce(initialValue, function)));
        }

        return all.stream()
                .map(CompletableFuture::join)
                .reduce(initialValue, function);
    }

    Stream<Value> stream();
}
