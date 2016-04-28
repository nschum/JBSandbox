package de.nschum.jbsandbox.interpreter;

import de.nschum.jbsandbox.ast.*;

import java.util.List;

/**
 * Evaluates side-effect free expressions.
 * <p/>
 * ExpressionEvaluator is designed to be shared among multiple threads.
 */
class ExpressionEvaluator {

    private volatile boolean cancelled;

    void cancel() {
        cancelled = true;
    }

    private void checkIfCancelled() {
        if (cancelled) {
            throw new InterpreterCancelledException();
        }
    }

    Value evaluateExpression(Expression expression, State state) {
        checkIfCancelled();
        if (expression instanceof IntLiteral) {
            return evaluateInt((IntLiteral) expression);
        } else if (expression instanceof FloatLiteral) {
            return evaluateFloat((FloatLiteral) expression);
        } else if (expression instanceof IntRangeExpression) {
            return evaluateIntRange((IntRangeExpression) expression, state);
        } else if (expression instanceof ParenthesizedExpression) {
            return evaluateParentheses((ParenthesizedExpression) expression, state);
        } else if (expression instanceof Reference) {
            return evaluateReference((Reference) expression, state);
        } else if (expression instanceof MapExpression) {
            return evaluateMap((MapExpression) expression, state);
        } else if (expression instanceof ReduceExpression) {
            return evaluateReduce((ReduceExpression) expression, state);
        } else if (expression instanceof OperationExpression) {
            return evaluateOperation((OperationExpression) expression, state);
        }
        throw new IllegalArgumentException();
    }

    private Value evaluateInt(IntLiteral expression) {
        return Value.of(expression.getContent());
    }

    private Value evaluateFloat(FloatLiteral expression) {
        return Value.of(expression.getContent());
    }

    private Value evaluateIntRange(IntRangeExpression expression, State state) {
        int lowerBound = evaluateExpression(expression.getLowerBound(), state).getIntValue();
        int upperBound = evaluateExpression(expression.getUpperBound(), state).getIntValue();

        if (lowerBound > upperBound) {
            throw new InterpreterRuntimeException("Invalid range, lower bound is greater than upper bound",
                    expression.getLocation());
        }
        return Value.of(new IntRange(lowerBound, upperBound));
    }

    private Value evaluateParentheses(ParenthesizedExpression expression, State state) {
        return evaluateExpression(expression.getExpression(), state);
    }

    private Value evaluateReference(Reference expression, State state) {
        return state.lookUp(expression.getVariable());
    }

    private Value evaluateMap(MapExpression expression, State state) {
        Value input = evaluateExpression(expression.getInput(), state);

        Type inputType = expression.getInput().getType();
        Type parameterType = expression.getFunction().getParameters().get(0).getType();

        return Value.of(((Sequence) promote(input, inputType, parameterType.asSequence()).get()).map(value -> {
            checkIfCancelled();
            return applyFunction(expression.getFunction(), value);
        }));
    }

    private Value evaluateReduce(ReduceExpression expression, State state) {
        Value initialValue = evaluateExpression(expression.getInitialValue(), state);
        Value input = evaluateExpression(expression.getInput(), state);

        Type inputType = expression.getInput().getType();
        Type parameterType = expression.getFunction().getParameters().get(0).getType();

        Value promotedInitialValue = promote(initialValue, expression.getInitialValue().getType(), parameterType);
        Sequence promotedInput = (Sequence) promote(input, inputType, parameterType.asSequence()).get();
        Lambda function = expression.getFunction();

        return promotedInput.reduce(promotedInitialValue, (v1, v2) -> {
            checkIfCancelled();
            return  applyFunction(function, v1, v2);
        });
    }

    private Value evaluateOperation(OperationExpression expression, State state) {

        Type lhsType = expression.getLeftHandSide().getType();
        Type rhsType = expression.getRightHandSide().getType();
        Type type = lhsType.equals(Type.FLOAT) ? Type.FLOAT : rhsType;

        // promote type for mixed operations
        Value lhs = promote(evaluateExpression(expression.getLeftHandSide(), state), lhsType, type);
        Value rhs = promote(evaluateExpression(expression.getRightHandSide(), state), rhsType, type);

        if (type.equals(Type.FLOAT)) {
            switch (expression.getOperation()) {
                case PLUS:
                    return Value.of(lhs.getFloatValue() + rhs.getFloatValue());
                case MINUS:
                    return Value.of(lhs.getFloatValue() - rhs.getFloatValue());
                case MULTIPLY:
                    return Value.of(lhs.getFloatValue() * rhs.getFloatValue());
                case DIVIDE:
                    return Value.of(lhs.getFloatValue() / rhs.getFloatValue());
                case EXP:
                    return Value.of(Math.pow(lhs.getFloatValue(), rhs.getFloatValue()));
            }
        } else {
            switch (expression.getOperation()) {
                case PLUS:
                    return Value.of(lhs.getIntValue() + rhs.getIntValue());
                case MINUS:
                    return Value.of(lhs.getIntValue() - rhs.getIntValue());
                case MULTIPLY:
                    return Value.of(lhs.getIntValue() * rhs.getIntValue());
                case DIVIDE:
                    if (rhs.getIntValue() == 0) {
                        throw new InterpreterRuntimeException("Division by zero", expression.getLocation());
                    }
                    return Value.of(lhs.getIntValue() / rhs.getIntValue());
                case EXP:
                    return Value.of(pow(lhs.getIntValue(), rhs.getIntValue()));
            }
        }
        throw new IllegalArgumentException();
    }

    private int pow(int base, int exp) {
        if (exp % 2 == 0) {
            if (exp == 0) {
                return 1;
            } else {
                return pow(base * base, exp / 2);
            }
        } else {
            return base * pow(base * base, exp / 2);
        }
    }

    /**
     * Promote a Value to an bigger type, e.g. INT -> DOUBLE, INT[] -> DOUBLE[]
     */
    private Value promote(Value value, Type from, Type to) {
        if (from.equals(to)) {
            return value;
        } else if (from.equals(Type.INT) && to.equals(Type.FLOAT)) {
            return Value.of((double) value.getIntValue());
        } else if (from.isSequence() && to.isSequence()) {
            return Value.of(((Sequence) (value.get())).map(v -> promote(v, from.getInnerType(), to.getInnerType())));
        } else {
            throw new IllegalArgumentException("Cannot convert " + from + " to " + to);
        }
    }

    /**
     * Evaluate a function with given arguments
     * <p/>
     * Functions do not access global variables and can therefore be parallelized.
     */
    private Value applyFunction(Lambda function, Value... arguments) {

        State state = new State();

        // store arguments as variables
        List<Variable> parameters = function.getParameters();
        assert arguments.length == parameters.size();
        for (int i = 0; i < arguments.length; i++) {
            Variable parameter = parameters.get(i);
            state.store(parameter, arguments[i]);
        }

        return evaluateExpression(function.getExpression(), state);
    }
}
