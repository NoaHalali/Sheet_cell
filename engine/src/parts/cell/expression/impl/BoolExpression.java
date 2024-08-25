package parts.cell.expression.impl;
import parts.cell.expression.effectiveValue.CellType;
import parts.cell.expression.effectiveValue.EffectiveValue;
import parts.cell.expression.effectiveValue.EffectiveValueImpl;
import parts.cell.expression.Expression;

import java.io.Serializable;

public class BoolExpression implements Expression, Serializable {
    private boolean flag;

    public BoolExpression(boolean flag) {
        //super(Boolean.toString(flag));
        this.flag = flag;
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
        //
        return new EffectiveValueImpl(CellType.BOOLEAN,flag ); // ערך אפקטיבי עבור ביטויים לוגיים
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.BOOLEAN;
    }
}