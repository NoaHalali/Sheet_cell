package parts.cell.impl;

import parts.cell.CellType;
import parts.cell.EffectiveValue;
import parts.cell.EffectiveValueImpl;
import parts.cell.Expression;

public class NumberExpression implements Expression {
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
