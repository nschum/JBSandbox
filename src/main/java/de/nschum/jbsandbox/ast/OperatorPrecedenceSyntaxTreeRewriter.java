package de.nschum.jbsandbox.ast;

/**
 * Rewrites the syntax tree for semantic reasens
 */
class OperatorPrecedenceSyntaxTreeRewriter {

    /**
     * Modifies the ParseTree to account for operator precedence
     * <p/>
     * This looks at the tree's right hand side and while the right hand side as also an operation and has lower or
     * equal precedence, it moves the ParseTree down the tree to the right position.
     */
    OperationExpression reorderOperations(OperationExpression expression) {

        if (expression.getRightHandSide() instanceof OperationExpression) {
            // The right hand expression is also an operation
            OperationExpression rightHandSide = (OperationExpression) expression.getRightHandSide();

            if (!rightHandSide.getOperation().hasHigherPrecedenceThan(expression.getOperation())) {
                // and it has lower precedence or equal precedence, so flip the tree so
                // expression is evaluated BEFORE right hand side / expression is BELOW right hand side on the tree
                //
                //                 expression
                //                  /       \
                //                LHS       RHS
                //                         /   \
                // becomes
                //
                //                      newRightHandSide
                //                         /       \
                //              newExpression
                //              /           \
                //            LHS

                Type newExpressionType = promoteTypes(expression.getLeftHandSide().getType(), rightHandSide.getLeftHandSide().getType());
                OperationExpression newExpression = reorderOperations(
                        new OperationExpression(
                                newExpressionType,
                                expression.getLeftHandSide(),
                                rightHandSide.getLeftHandSide(),
                                expression.getOperation(),
                                expression.getTerminals()));

                Type newRightHandSideType = promoteTypes(rightHandSide.getType(), expression.getType());
                OperationExpression newRightHandSide = new OperationExpression(
                        newRightHandSideType,
                        newExpression,
                        rightHandSide.getRightHandSide(),
                        rightHandSide.getOperation(),
                        rightHandSide.getTerminals());

                return newRightHandSide;
            }
        }

        return expression;
    }

    private Type promoteTypes(Type type1, Type type2) {
        if (type1.equals(Type.UNDETERMINED) || type2.equals(Type.UNDETERMINED)) {
            return Type.UNDETERMINED;
        } else if (type1.canBeAssignedFrom(type2)) {
            return type1;
        } else if (type2.canBeAssignedFrom(type1)) {
            return type2;
        } else {
            return Type.UNDETERMINED;
        }
    }

}
