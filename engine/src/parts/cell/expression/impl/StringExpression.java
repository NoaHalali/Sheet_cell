package parts.cell.expression.impl;

import parts.cell.CellType;
import parts.cell.expression.effectiveValue.EffectiveValue;
import parts.cell.expression.effectiveValue.EffectiveValueImpl;
import parts.cell.expression.Expression;

import java.io.Serializable;

public class StringExpression implements Expression, Serializable {
    String value;
    public StringExpression(String value) {
        this.value =value;
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
        return new EffectiveValueImpl(CellType.STRING, value);
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.STRING;
    }
}
