package de.nschum.jbsandbox.interpreter;

import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * A sequence that is created by applying a function to another sequence
 * <p/>
 * No values are stored, just the original (immutable) sequence and the transforming function.
 * That reduces the need to allocate large chunks of memory.
 */
public class LazySequence implements Sequence {

    private Sequence base;
    private Function<? super Value, ? extends Value> lambda;

    public LazySequence(Sequence base, Function<? super Value, ? extends Value> lambda) {
        this.base = base;
        this.lambda = lambda;
    }

    @Override
    public int size() {
        return base.size();
    }

    @Override
    public Stream<Value> stream() {
        return base.stream().map(lambda);
    }

    @Override
    public String toString() {
        // Iterate over the LazySequence in parallel.
        // Because the values haven't been calculated on creation, parallelization needs to happen on output
        // (or when reducing the sequence)
        return "[" + stream().parallel().map(Value::toString).collect(joining(",")) + "]";
    }
}
