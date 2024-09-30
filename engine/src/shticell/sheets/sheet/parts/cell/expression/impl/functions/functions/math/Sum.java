package shticell.sheets.sheet.parts.cell.expression.impl.functions.functions.math;

import shticell.sheets.sheet.parts.Range;
import shticell.sheets.sheet.parts.cell.expression.Expression;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.CellType;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValueImpl;

import java.io.Serializable;

public class Sum implements Expression, Serializable {
    private Range range;

    public Sum(Range range) {
        this.range = range;

    }


    @Override
    public EffectiveValue calculateEffectiveValue() {
        return new EffectiveValueImpl(CellType.NUMERIC,range.calculateCellsSum());

    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }

}
