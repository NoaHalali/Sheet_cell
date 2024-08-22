package parts.cell.impl;

import parts.cell.CellType;
import parts.cell.EffectiveValue;
import parts.cell.EffectiveValueImpl;

public class StringExpression extends AbstractExpression {
    public StringExpression(String value) {
        super(value);
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {

            return new EffectiveValueImpl(CellType.STRING, originalValue); // חישוב ערך אפקטיבי, במקרה זה חיתוך רווחים

    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.STRING;
    }
}
