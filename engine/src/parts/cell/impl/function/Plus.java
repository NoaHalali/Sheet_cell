package parts.cell.impl.function;

import parts.cell.Expression;
import parts.cell.impl.Number;

public class Plus extends BinaryExpression {
    public Plus(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public Expression evaluate(Expression arg1, Expression arg2) {
        Number num1, num2;
        num1 = (Number) arg1.evaluate();
        num2 = (Number) arg2.evaluate();
        return new Number(num1.getNum() + num2.getNum());

    }



}
