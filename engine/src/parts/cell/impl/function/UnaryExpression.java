package parts.cell.impl.function;

import parts.cell.EffectiveValue;
import parts.cell.Expression;
import parts.cell.impl.AbstractExpression;

public  abstract class UnaryExpression extends AbstractExpression {
    protected Expression exp;

    @Override
    protected abstract EffectiveValue calculateEffectiveValue();

    public EffectiveValue evaluate() {
        return effectiveValue; // מחזיר את הערך המוערך שכבר חושב
    }
}
