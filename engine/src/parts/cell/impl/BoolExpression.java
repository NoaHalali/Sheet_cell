package parts.cell.impl;
import parts.cell.EffectiveValue;
import parts.cell.EffectiveValueImpl;
import parts.cell.Expression;

public class BoolExpression extends AbstractExpression {
    private boolean flag;

    public BoolExpression(boolean flag) {
        super(Boolean.toString(flag));
        this.flag = flag;
    }

    @Override
    protected EffectiveValue calculateEffectiveValue() {
        //
        return new EffectiveValueImpl(CellType.BOOLEAN,originalValue.toLowerCase()=="true" ); // ערך אפקטיבי עבור ביטויים לוגיים
    }
}