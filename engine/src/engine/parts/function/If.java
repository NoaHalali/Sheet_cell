package engine.parts.function;

import engine.parts.sheet.cell.expression.Expression;
import engine.parts.sheet.cell.expression.effectiveValue.CellType;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValue;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValueImpl;

public class If extends TernaryExpression{

    public If(Expression expression1, Expression expression2, Expression expression3) {

        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
        try {
            boolean flag = expression1.calculateEffectiveValue().extractValueWithExpectation(Boolean.class);
            EffectiveValue leftVal = expression2.calculateEffectiveValue();
            EffectiveValue rightVal = expression3.calculateEffectiveValue();
            if (leftVal.getCellType() != rightVal.getCellType()) {
                return new EffectiveValueImpl(CellType.STRING, "UNKNOWN");
            }

            return  flag ? leftVal : rightVal;

        } catch (Exception e) {
            return new EffectiveValueImpl(CellType.STRING, "UNKNOWN");
        }
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.BOOLEAN;
    }
}
