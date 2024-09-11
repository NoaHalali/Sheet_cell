package parts.function;

import parts.Range;
import parts.cell.expression.Expression;
import parts.cell.expression.effectiveValue.CellType;
import parts.cell.expression.effectiveValue.EffectiveValue;
import parts.cell.expression.effectiveValue.EffectiveValueImpl;

import java.io.Serializable;

public class Average implements Expression, Serializable {
    private Range range;

    public Average(Range range) {
        this.range = range;

    }


    @Override
    public EffectiveValue calculateEffectiveValue() {
        return new EffectiveValueImpl(CellType.NUMERIC,range.calculateCellsAverage());

    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }
}
