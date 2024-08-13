package parts.cell.impl;
import parts.cell.Expression;

public class BoolExpression extends AbstractExpression {
    private boolean flag;

    public BoolExpression(boolean flag) {
        super(Boolean.toString(flag));
        this.flag = flag;
    }

    @Override
    protected String calculateEffectiveValue() {
        return flag ? "TRUE" : "FALSE"; // ערך אפקטיבי עבור ביטויים לוגיים
    }
}