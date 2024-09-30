package engine.parts.sheet.cell.expression.impl;

import engine.parts.sheet.cell.expression.effectiveValue.CellType;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValue;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValueImpl;
import engine.parts.sheet.cell.expression.Expression;

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
