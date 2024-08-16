package parts.cell.impl.function;

import parts.cell.EffectiveValue;
import parts.cell.Expression;
import parts.cell.impl.AbstractExpression;

public abstract class TernaryExpression extends AbstractExpression {
    protected Expression exp1;
    protected Expression exp2;
    protected Expression exp3;
    public TernaryExpression(Expression exp1,Expression exp2,Expression exp3) {
        //super(originalValue);
       this.exp1 = exp1;
       this.exp2 = exp2;
       this.exp3 = exp3;
        //this.originalValue = calculateOriginalValue();
        this.effectiveValue = calculateEffectiveValue();
    }
    @Override
    protected abstract EffectiveValue calculateEffectiveValue();
}
