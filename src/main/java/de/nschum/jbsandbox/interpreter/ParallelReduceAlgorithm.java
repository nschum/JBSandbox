package de.nschum.jbsandbox.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

/**
 * Perform an reduce operation on an associative function
 */
public class ParallelReduceAlgorithm<T> {

    public T reduce(Stream<T> stream, T initialValue, BinaryOperator<T> function) {
        long size = stream.count();
        //int cores = Runtime.getRuntime().availableProcessors();
        int chunkSize = 100;//Math.max(1, input.count() / cores);

        List<CompletableFuture<T>> all = new ArrayList<>();
        for (int i = 0; i < size; i += chunkSize) {
            long limit = Math.min(chunkSize, size - i);
            final Stream<T> substream = stream.skip(i).limit(limit);
            all.add(CompletableFuture.supplyAsync(() -> substream.reduce(initialValue, function)));
        }

        return all.stream()
                .map(CompletableFuture::join)
                .reduce(initialValue, function);
/*        return new SequenceImpl(all.stream().map(CompletableFuture::join).flatMap(l -> l.stream()).collect(toList()));


        T reducedValue = initialValue;
        for (T value : stream) {
            reducedValue = function.apply(reducedValue, value);
        }
        return reducedValue;*/
    }
}
