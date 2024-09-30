package engine.parts.function;

import engine.parts.sheet.cell.expression.Expression;
import engine.parts.sheet.cell.expression.effectiveValue.CellType;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValue;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValueImpl;

public class Equal extends BinaryExpression{
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