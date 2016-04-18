package de.nschum.jbsandbox.ast;

class OperatorPrecedenceSyntoxTreeRewriter {

    /**
     * Modifies the ParseTree to account for operator precedence
     * <p>
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

                OperationExpression newExpression = reorderOperations(
                        new OperationExpression(
                                expression.getType(),
                                expression.getLeftHandSide(),
                                rightHandSide.getLeftHandSide(),
                                expression.getOperation()));

                OperationExpression newRightHandSide = new OperationExpression(
                        rightHandSide.getType(),
                        newExpression,
                        rightHandSide.getRightHandSide(),
                        rightHandSide.getOperation());

                return newRightHandSide;
            }
        }

        return expression;
    }
}
