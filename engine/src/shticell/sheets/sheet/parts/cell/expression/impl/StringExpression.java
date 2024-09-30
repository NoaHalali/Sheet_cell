package shticell.sheets.sheet.parts.cell.expression.impl;

import shticell.sheets.sheet.parts.cell.expression.effectiveValue.CellType;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValueImpl;
import shticell.sheets.sheet.parts.cell.expression.Expression;

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
