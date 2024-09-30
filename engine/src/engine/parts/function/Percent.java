package engine.parts.function;

import engine.parts.sheet.cell.expression.Expression;
import engine.parts.sheet.cell.expression.effectiveValue.CellType;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValue;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValueImpl;

public class Percent  extends BinaryExpression{
    public Percent(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public EffectiveValue calculateEffectiveValue() {
        try {
            EffectiveValue leftValue = left.calculateEffectiveValue();
            EffectiveValue rightValue = right.calculateEffectiveValue();
            double result = leftValue.extractValueWithExpectation(Double.class) * rightValue.extractValueWithExpectation(Double.class);
            result/=100;
            return new EffectiveValueImpl(CellType.NUMERIC, result);
        }
        catch (Exception e) {
            return new EffectiveValueImpl(CellType.NUMERIC, Double.NaN);
        }    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }
}
