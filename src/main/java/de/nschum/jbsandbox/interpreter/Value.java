package de.nschum.jbsandbox.interpreter;

/**
 * Holder for all values in the program state
 * <p/>
 * For optimization (to anvoid constant boxing and unboxing), specific classes for INT and FLOAT are used.
 * Static factory methods for these are provided.
 */
public interface Value {
    static Value of(Object value) {
        return new ObjectValue(value);
    }

    static Value of(double value) {
        return new FloatValue(value);
    }

    static Value of(int value) {
        return new IntValue(value);
    }

    Object get();
    int getIntValue();
    double getFloatValue();

    class ObjectValue implements Value {
        private Object value;

        private ObjectValue(Object value) {
            this.value = value;
        }

        @Override
        public Object get() {
            return value;
        }

        @Override
        public int getIntValue() {
            return (Integer) value;
        }

        @Override
        public double getFloatValue() {
            return (Double) value;
        }

        @Override
        public String toString() {
            return get().toString();
        }
    }

    class IntValue implements Value {
        private int value;

        public IntValue(int value) {
            this.value = value;
        }

        @Override
        public Object get() {
            return value;
        }

        @Override
        public int getIntValue() {
            return value;
        }

        @Override
        public double getFloatValue() {
            return (double) value;
        }

        @Override
        public String toString() {
            return get().toString();
        }
    }

    class FloatValue implements Value {
        private double value;

        public FloatValue(double value) {
            this.value = value;
        }

        @Override
        public Object get() {
            return value;
        }

        @Override
        public int getIntValue() {
            throw new IllegalArgumentException("Must not use FLOAT as INT");
        }

        @Override
        public double getFloatValue() {
            return value;
        }

        @Override
        public String toString() {
            return get().toString();
        }
    }
}
