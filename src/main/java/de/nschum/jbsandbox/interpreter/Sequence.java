package de.nschum.jbsandbox.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * A sequence of values
 */
public interface Sequence {

    int size();

    default Sequence map(Function<? super Value, ? extends Value> function) {
        return new SequenceImpl(stream().map(function).collect(toList()));
    }

    default Sequence parallelMap(Function<? super Value, ? extends Value> function) {
        // stream().parallel() is faster for small sequences
        // Small chunk sizes are much slower, because we are memory-limited and it increases memory overhead.

        int size = size();
        int cores = Runtime.getRuntime().availableProcessors();
        int chunkSize = Math.max(1, size / cores);

        List<CompletableFuture<List<Value>>> all = new ArrayList<>();
        for (int i = 0; i < size; i += chunkSize) {
            int limit = Math.min(chunkSize, size - i);
            final Stream<Value> substream = stream().skip(i).limit(limit);
            all.add(CompletableFuture.supplyAsync(() -> substream.map(function).collect(toList())));
        }

        return new SequenceImpl(all.stream()
                .map(CompletableFuture::join)
                .flatMap(l -> l.stream())
                .collect(toList()));
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
