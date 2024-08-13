package parts.cell.impl.function;

import parts.cell.Expression;
import parts.cell.impl.Number;

public class Plus extends BinaryExpression {
    public Plus(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String calculateOriginalValue() {
        double leftVal = Double.parseDouble(left.evaluate().replace(",", ""));
        double rightVal = Double.parseDouble(right.evaluate().replace(",", ""));
        return String.valueOf(leftVal + rightVal);
    }
}
