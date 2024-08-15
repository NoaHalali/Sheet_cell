package parts.cell.impl;
import parts.cell.EffectiveValue;
import parts.cell.Expression;

public abstract class AbstractExpression implements Expression {
    protected String originalValue;
    protected EffectiveValue effectiveValue;

    //לא בטוח שצריך בכלל את הערך המקורי
    public AbstractExpression(String originalValue) {
        this.originalValue = originalValue;
        this.effectiveValue = calculateEffectiveValue();
    }
    public AbstractExpression(){};

    protected abstract EffectiveValue calculateEffectiveValue();

    @Override
    public EffectiveValue evaluate() {
        return effectiveValue;
    }

    @Override
    public String getOriginalValue() {
        return originalValue;
    }

    //?
    public void setOriginalValue(Expression originalValue) {
        this.originalValue = originalValue.getOriginalValue();
        this.effectiveValue = calculateEffectiveValue();
    }
}