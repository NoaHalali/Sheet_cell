package parts.function;

import parts.sheet.cell.expression.Expression;
import parts.sheet.cell.expression.effectiveValue.CellType;
import parts.sheet.cell.expression.effectiveValue.EffectiveValue;
import parts.sheet.cell.expression.effectiveValue.EffectiveValueImpl;

public class And extends BinaryExpression{
    public And(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
    @Override
    public EffectiveValue calculateEffectiveValue() {
        try {
            EffectiveValue leftValue = left.calculateEffectiveValue();
            EffectiveValue rightValue = right.calculateEffectiveValue();
            boolean leftFlag = leftValue.extractValueWithExpectation(Boolean.class);
            boolean rightFlag = rightValue.extractValueWithExpectation(Boolean.class);
            boolean flag = leftFlag && rightFlag;
            return new EffectiveValueImpl(CellType.BOOLEAN,flag);
        } catch (Exception e) {
            return new EffectiveValueImpl(CellType.STRING,"UNKNOWN");
        }
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.BOOLEAN;
    }
}
