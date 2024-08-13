package parts.cell.impl;

public class StringExpression extends AbstractExpression {
    public StringExpression(String value) {
        super(value);
    }

    @Override
    protected String calculateEffectiveValue() {
        return originalValue.trim(); // חישוב ערך אפקטיבי, במקרה זה חיתוך רווחים
    }
}
