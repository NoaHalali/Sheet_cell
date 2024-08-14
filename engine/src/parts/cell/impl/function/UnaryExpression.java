package parts.cell.impl.function;

import parts.cell.Expression;
import parts.cell.impl.AbstractExpression;

public  abstract class UnaryExpression extends AbstractExpression {
    protected Expression exp;

    @Override
    protected abstract String calculateEffectiveValue();

    public String evaluate() {
        return effectiveValue; // מחזיר את הערך המוערך שכבר חושב
    }
}
