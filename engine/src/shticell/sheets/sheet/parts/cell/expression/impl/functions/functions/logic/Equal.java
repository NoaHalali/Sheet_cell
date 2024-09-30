package shticell.sheets.sheet.parts.cell.expression.impl.functions.functions.logic;

import shticell.sheets.sheet.parts.cell.expression.Expression;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.CellType;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValueImpl;
import shticell.sheets.sheet.parts.cell.expression.impl.functions.BinaryExpression;

public class Equal extends BinaryExpression {
    public Equal(Expression left, Expression right) {
        this.left = left;
        this.right = right;

    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
        try{
            EffectiveValue leftVal = left.calculateEffectiveValue();
            EffectiveValue rightVal = right.calculateEffectiveValue();
            return new EffectiveValueImpl(CellType.BOOLEAN, leftVal.equals(rightVal));

        }catch (Exception e){
            return new EffectiveValueImpl(CellType.STRING,"UNKNOWN");
        }
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.BOOLEAN;
    }
}
