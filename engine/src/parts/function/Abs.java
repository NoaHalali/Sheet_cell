package parts.function;

import parts.cell.expression.effectiveValue.EffectiveValue;
import parts.cell.expression.effectiveValue.EffectiveValueImpl;
import parts.cell.expression.Expression;
import parts.cell.expression.effectiveValue.CellType;

public class Abs extends UnaryExpression{

    public Abs(Expression exp) {

        CellType expCellType = exp.getFunctionResultType();

        if ( (!expCellType.equals(CellType.NUMERIC) && !expCellType.equals(CellType.UNKNOWN)) ) {
            throw new IllegalArgumentException("Invalid argument types for ABS function. Expected to get one arguments from type NUMERIC, but got " + expCellType);
        }
        this.exp = exp;


    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
        EffectiveValue Value = exp.calculateEffectiveValue();
        try {
            double result = Math.abs(Value.extractValueWithExpectation(Double.class));
            return new EffectiveValueImpl(CellType.NUMERIC, result);
        }  catch(ClassCastException e) {
            throw new ClassCastException(e.getMessage() + "ABS function expected to get one arguments from type NUMERIC, but got " +Value.getCellType());
        }
    }
    @Override
    public  CellType getFunctionResultType(){
        return CellType.NUMERIC;
    }
}
