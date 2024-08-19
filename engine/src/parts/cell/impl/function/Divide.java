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
    protected EffectiveValue calculateEffectiveValue()throws ArithmeticException {//בדיקה שלא מחלקים ב0- ומידה וכן יוציא אקספשיון ויזרק למשתמש בUI
        try {
            EffectiveValue leftValue = left.evaluate();
            EffectiveValue rightValue = right.evaluate();
            if (rightValue.extractValueWithExpectation(Double.class) == 0) {
                throw new ArithmeticException("Divide by zero");
            }
            double result = leftValue.extractValueWithExpectation(Double.class) / rightValue.extractValueWithExpectation(Double.class);
        }catch (Exception EX){

        }

        return  new EffectiveValueImpl(CellType.NUMERIC, result);
    }
}
