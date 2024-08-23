package parts.function;

import parts.cell.expression.effectiveValue.EffectiveValue;
import parts.cell.expression.effectiveValue.EffectiveValueImpl;
import parts.cell.expression.Expression;
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
