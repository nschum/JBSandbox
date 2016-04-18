package de.nschum.jbsandbox.ast;

/**
 * A type in the abstract syntax tree
 */
public interface Type {
    Type UNDETERMINED = new NumericType("UNDETERMINED"); // used if a type error prevents type inference
    Type INT = new NumericType("INT");
    Type FLOAT = new NumericType("FLOAT");

    Type INT_SEQUENCE = INT.asSequence();

    /**
     * Tests if variable of this type can be assigned with a value of type "type"
     */
    boolean canBeAssignedFrom(Type type);

    /**
     * Tests if type can be used to perform arithmetic calculations
     */
    boolean canPerformArithmetic();

    boolean isSequence();

    Type getInnerType();

    default Type asSequence() {
        return new SequenceType(this);
    }
}

class NumericType implements Type {

    private String name;

    NumericType(String name) {
        this.name = name;
    }

    public boolean canPerformArithmetic() {
        return true;
    }

    @Override
    public boolean isSequence() {
        return false;
    }

    @Override
    public Type getInnerType() {
        return UNDETERMINED;
    }

    @Override
    public boolean canBeAssignedFrom(Type type) {
        return equals(type) || (this == FLOAT && type == INT);
    }

    @Override
    public String toString() {
        return name;
    }
}

class SequenceType implements Type {
    private Type innerType;

    SequenceType(Type innerType) {
        assert innerType != null;
        this.innerType = innerType;
    }

    public boolean canPerformArithmetic() {
        return false;
    }

    @Override
    public boolean isSequence() {
        return true;
    }

    @Override
    public Type getInnerType() {
        return innerType;
    }

    @Override
    public boolean canBeAssignedFrom(Type type) {
        return type instanceof SequenceType && innerType.canBeAssignedFrom(((SequenceType) type).innerType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SequenceType sequenceType = (SequenceType) o;

        return innerType.equals(sequenceType.innerType);

    }

    @Override
    public int hashCode() {
        return innerType.hashCode();
    }

    @Override
    public String toString() {
        return innerType.toString() + "[]";
    }
}
