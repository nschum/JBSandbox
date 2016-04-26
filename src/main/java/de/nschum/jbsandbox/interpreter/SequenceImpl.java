package de.nschum.jbsandbox.interpreter;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * A sequence that actually stores each value
 * <p>
 * This requires storage of O(n) for n values.
 */
class SequenceImpl implements Sequence {

    final private List<Value> values;

    public SequenceImpl(List<Value> values) {
        assert values != null;
        this.values = values;
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public Value get(int i) {
        return values.get(i);
    }

    @Override
    public Stream<Value> stream() {
        return values.stream();
    }

    @Override
    public String toString() {
        return "[" + stream().map(Value::get).map(Object::toString).collect(joining(",")) + "]";
    }
}
