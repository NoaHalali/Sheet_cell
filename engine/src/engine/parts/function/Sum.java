package engine.parts.function;

import engine.parts.Range;
import engine.parts.sheet.cell.expression.Expression;
import engine.parts.sheet.cell.expression.effectiveValue.CellType;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValue;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValueImpl;

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
