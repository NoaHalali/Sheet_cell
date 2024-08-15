package parts.cell.impl.function;

import parts.cell.EffectiveValue;
import parts.cell.EffectiveValueImpl;
import parts.cell.Expression;
import parts.cell.impl.CellType;

public class Divide extends BinaryExpression{
    public Divide(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue calculateEffectiveValue() {//בדיקה שלא מחלקים ב0- ומידה וכן יוציא אקספשיון ויזרק למשתמש בUI
        EffectiveValue leftValue = left.evaluate();
        EffectiveValue rightValue=right.evaluate();
        double result = leftValue.extractValueWithExpectation(Double.class) / rightValue.extractValueWithExpectation(Double.class);
        return  new EffectiveValueImpl(CellType.NUMERIC, result);
    }
}
