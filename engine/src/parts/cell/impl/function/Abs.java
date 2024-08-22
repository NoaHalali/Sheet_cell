package parts.cell.impl.function;

import parts.cell.EffectiveValue;
import parts.cell.EffectiveValueImpl;
import parts.cell.Expression;
import parts.cell.CellType;

public class Abs extends UnaryExpression{

    public Abs(Expression exp) {

        CellType expCellType = exp.getFunctionResultType();

        if ( (!expCellType.equals(CellType.NUMERIC) && !expCellType.equals(CellType.UNKNOWN)) ) {
            throw new IllegalArgumentException("Invalid argument types for MOD function. Expected NUMERIC, but got " + expCellType);
        }
        this.exp=exp;


    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
        EffectiveValue Value = exp.calculateEffectiveValue();

        double result = Math.abs(Value.extractValueWithExpectation(Double.class) );
        return  new EffectiveValueImpl(CellType.NUMERIC, result);
    }
    @Override
    public  CellType getFunctionResultType(){
        return CellType.NUMERIC;
    }
}
