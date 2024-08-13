package parts.cell.impl.function;

import parts.cell.Expression;

public class Divide extends BinaryExpression{
    public Divide(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected String calculateEffectiveValue() {//בדיקה שלא מחלקים ב0- ומידה וכן יוציא אקספשיון ויזרק למשתמש בUI
        double leftVal = Double.parseDouble(left.evaluate().replace(",", ""));
        double rightVal = Double.parseDouble(right.evaluate().replace(",", ""));
        return String.valueOf(leftVal / rightVal);
    }
}
