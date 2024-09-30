package shticell.sheets.sheet.parts.cell.expression.impl.functions.functions.logic;

import shticell.sheets.sheet.parts.cell.expression.Expression;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.CellType;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValueImpl;
import shticell.sheets.sheet.parts.cell.expression.impl.functions.BinaryExpression;

public class Or extends BinaryExpression {
    public Or(Expression left, Expression right) {
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
            boolean flag = leftFlag || rightFlag;
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
