package de.nschum.jbsandbox.interpreter;

import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * A sequence of values
 */
public interface Sequence extends Iterable<Value> {

    int size();

    Value get(int i);

    default Sequence map(Function<? super Value, ? extends Value> function) {
        return new SequenceImpl(stream().map(function).collect(toList()));
    }

    Stream<Value> stream();

    @Override
    default Iterator<Value> iterator() {
        return stream().iterator();
    }
}
