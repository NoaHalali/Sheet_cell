package parts.cell.expression.impl;

import parts.cell.expression.effectiveValue.CellType;
import parts.cell.expression.effectiveValue.EffectiveValue;
import parts.cell.expression.effectiveValue.EffectiveValueImpl;
import parts.cell.expression.Expression;

import java.io.Serializable;

public class NumberExpression implements Expression, Serializable {
    private double num;

    //or get String and then Parse to double
    public NumberExpression(double num) {

        this.num = num;
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
//num =Double.parseDouble(originalValue);?????
    return new EffectiveValueImpl(CellType.NUMERIC,num);
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }
}
