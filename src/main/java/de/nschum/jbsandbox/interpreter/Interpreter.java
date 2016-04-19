package de.nschum.jbsandbox.interpreter;

import de.nschum.jbsandbox.ast.*;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Executes a program defined by its abstract syntax tree
 */
public class Interpreter {

    private Writer outputWriter;

    public Interpreter(Writer outputWriter) {
        this.outputWriter = outputWriter;
    }

    public void execute(Program program) throws IOException, InterpreterRuntimeException {
        State state = new State();
        for (Statement statement : program.getStatements()) {
            execute(statement, state);
        }
        outputWriter.flush();
    }

    private void execute(Statement statement, State state) throws IOException {
        if (statement instanceof PrintStatement) {
            executePrintStatement((PrintStatement) statement);
        } else if (statement instanceof OutStatement) {
            executeOutStatement((OutStatement) statement, state);
        } else if (statement instanceof Declaration) {
            executeDeclaration((Declaration) statement, state);
        }
    }

    private void executePrintStatement(PrintStatement statement) throws IOException {
        outputWriter.write(statement.getString());
    }

    private void executeOutStatement(OutStatement statement, State state) throws IOException {
        outputWriter.write(evaluateExpression(statement.getExpression(), state).toString());
    }

    private void executeDeclaration(Declaration statement, State state) {
        state.store(statement.getVariable(), evaluateExpression(statement.getExpression(), state));
    }

    private Value evaluateExpression(Expression expression, State state) {
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
        return new Value(expression.getContent());
    }

    private Value evaluateFloat(FloatLiteral expression) {
        return new Value(expression.getContent());
    }

    private Value evaluateIntRange(IntRangeExpression expression, State state) {
        int lowerBound = (Integer) evaluateExpression(expression.getLowerBound(), state).get();
        int upperBound = (Integer) evaluateExpression(expression.getUpperBound(), state).get();

        if (lowerBound > upperBound) {
            throw new InterpreterRuntimeException("Invalid range, lower bound is greater than upper bound");
        }
        return new Value(new IntRange(lowerBound, upperBound));
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

        return new Value(((Sequence) promote(input, inputType, parameterType.asSequence()).get()).map(value -> {
            return applyFunction(expression.getFunction(), state, value);
        }));
    }

    private Value evaluateReduce(ReduceExpression expression, State state) {
        Value initialValue = evaluateExpression(expression.getInitialValue(), state);
        Value input = evaluateExpression(expression.getInput(), state);

        Type inputType = expression.getInput().getType();
        Type parameterType = expression.getFunction().getParameters().get(0).getType();

        Value reducedValue = promote(initialValue, expression.getInitialValue().getType(), parameterType);
        for (Value value : (Sequence) promote(input, inputType, parameterType.asSequence()).get()) {
            reducedValue = applyFunction(expression.getFunction(), state, reducedValue, value);
        }
        return reducedValue;
    }

    private Value evaluateOperation(OperationExpression expression, State state) {

        Type lhsType = expression.getLeftHandSide().getType();
        Type rhsType = expression.getRightHandSide().getType();
        Type type = lhsType.equals(Type.FLOAT) ? Type.FLOAT : rhsType;

        // promote type for mixed operations
        Object lhs = promote(evaluateExpression(expression.getLeftHandSide(), state), lhsType, type).get();
        Object rhs = promote(evaluateExpression(expression.getRightHandSide(), state), rhsType, type).get();

        if (type.equals(Type.FLOAT)) {
            switch (expression.getOperation()) {
                case PLUS:
                    return new Value((Double) lhs + (Double) rhs);
                case MINUS:
                    return new Value((Double) lhs - (Double) rhs);
                case MULTIPLY:
                    return new Value((Double) lhs * (Double) rhs);
                case DIVIDE:
                    return new Value((Double) lhs / (Double) rhs);
                case EXP:
                    return new Value(Math.pow((Double) lhs, (Double) rhs));
            }
        } else {
            switch (expression.getOperation()) {
                case PLUS:
                    return new Value((Integer) lhs + (Integer) rhs);
                case MINUS:
                    return new Value((Integer) lhs - (Integer) rhs);
                case MULTIPLY:
                    return new Value((Integer) lhs * (Integer) rhs);
                case DIVIDE:
                    if ((Integer) rhs == 0) {
                        throw new InterpreterRuntimeException("Division by zero");
                    }
                    return new Value((Integer) lhs / (Integer) rhs);
                case EXP:
                    return new Value(pow((Integer) lhs, (Integer) rhs));
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
            return new Value((double) ((Integer) value.get()));
        } else if (from.isSequence() && to.isSequence()) {
            return new Value(((Sequence) (value.get())).map(v -> promote(v, from.getInnerType(), to.getInnerType())));
        } else {
            throw new IllegalArgumentException("Cannot convert " + from + " to " + to);
        }
    }

    private Value applyFunction(Lambda function, State parentState, Value... arguments) {

        State state = parentState.openNewScope();

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