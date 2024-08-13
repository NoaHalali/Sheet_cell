package parts.cell.impl.function;

import parts.cell.Expression;

import java.security.PublicKey;

public abstract class BinaryExpression implements Expression {

    private Expression expression1;
    private Expression expression2;

    public BinaryExpression(Expression expression1, Expression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public Expression evaluate() {
        return evaluate(expression1.evaluate(), expression2.evaluate());
    }

    abstract public Expression evaluate(Expression arg1, Expression arg2);

//    @Override
//    public String toString() {
//        return "(" + expression1 + getOperationSign() + expression2 + ")";
//    }



}

