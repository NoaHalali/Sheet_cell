package parts.cell.impl;
import parts.cell.CellType;
import parts.cell.EffectiveValue;
import parts.cell.EffectiveValueImpl;
import parts.cell.Expression;

public class BoolExpression implements Expression {
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