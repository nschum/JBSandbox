package de.nschum.jbsandbox.interpreter;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A sequence of consecutive ints
 * <p>
 * This only requires storage of O(1) for n consecutive ints.
 */
public class IntRange implements Sequence {

    private int lowerBound;
    private int upperBound;

    public IntRange(int lowerBound, int upperBound) {
        assert lowerBound <= upperBound;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public int size() {
        return upperBound - lowerBound + 1;
    }

    @Override
    public Value get(int i) {
        return new Value(lowerBound + i);
    }

    @Override
    public Stream<Value> stream() {
        return IntStream.range(lowerBound, upperBound + 1).mapToObj(i -> new Value(i));
    }


    @Override
    public String toString() {
        return "{" + lowerBound + "," + upperBound + "}";
    }
}
