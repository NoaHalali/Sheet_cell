package parts.sheet.cell.expression.impl;
import parts.sheet.cell.expression.effectiveValue.CellType;
import parts.sheet.cell.expression.effectiveValue.EffectiveValue;
import parts.sheet.cell.expression.effectiveValue.EffectiveValueImpl;
import parts.sheet.cell.expression.Expression;

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