package parts.cell.impl;

import parts.cell.CellType;
import parts.cell.EffectiveValue;
import parts.cell.EffectiveValueImpl;
import parts.cell.Expression;

public class StringExpression implements Expression {
    String value;
    public StringExpression(String value) {
        this.value=value;
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {

            return new EffectiveValueImpl(CellType.STRING, value); // חישוב ערך אפקטיבי, במקרה זה חיתוך רווחים

    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.STRING;
    }
}
