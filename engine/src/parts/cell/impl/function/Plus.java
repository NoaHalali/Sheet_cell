package parts.cell.impl.function;

import parts.cell.Expression;
import parts.cell.impl.Number;

public class Plus extends BinaryExpression {
    @Override
    public Expression evaluate(Expression arg1, Expression arg2) {
        double num1, num2, res ;
        num1 = arg1.getValue();
        num2 = arg2.getValue();
        res = (double)num1 + num2;
        return new Number(res);

//       arg1.evaluate() ;
//       arg2.evaluate();
    }
}
