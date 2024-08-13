package parts.cell.impl.function;

import parts.cell.Expression;

public class Mod  extends BinaryExpression{
    public Mod(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected String calculateEffectiveValue() {
        double leftVal = Double.parseDouble(left.evaluate().replace(",", ""));
        double rightVal = Double.parseDouble(right.evaluate().replace(",", ""));
        return String.valueOf(leftVal % rightVal);
    }
}
