package parts.cell.impl;
import parts.cell.Expression;

public abstract class AbstractExpression implements Expression {
    protected String originalValue;
    protected String effectiveValue;

    public AbstractExpression(String originalValue) {
        this.originalValue = originalValue;
        this.effectiveValue = calculateEffectiveValue();
    }

    protected abstract String calculateEffectiveValue();

    @Override
    public String evaluate() {
        return effectiveValue;
    }

    @Override
    public String getOriginalValue() {
        return originalValue;
    }

    //?
    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
        this.effectiveValue = calculateEffectiveValue();
    }
}