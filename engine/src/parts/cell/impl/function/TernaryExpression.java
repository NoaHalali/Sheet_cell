package parts.cell.impl.function;

import parts.cell.EffectiveValue;
import parts.cell.Expression;
import parts.cell.impl.AbstractExpression;

public abstract class TernaryExpression extends AbstractExpression {
    protected Expression exp1;
    protected Expression exp2;
    protected Expression exp3;
    @Override
    protected abstract EffectiveValue calculateEffectiveValue();
}
