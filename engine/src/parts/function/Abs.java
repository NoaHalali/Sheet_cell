package parts.function;

import parts.sheet.cell.expression.effectiveValue.EffectiveValue;
import parts.sheet.cell.expression.effectiveValue.EffectiveValueImpl;
import parts.sheet.cell.expression.Expression;
import parts.sheet.cell.expression.effectiveValue.CellType;

public class Abs extends UnaryExpression{

    public Abs(Expression exp) {

//        CellType expCellType = exp.getFunctionResultType();

//        if ( (!expCellType.equals(CellType.NUMERIC) && !expCellType.equals(CellType.UNKNOWN)) ) {
//            throw new IllegalArgumentException("Invalid argument types for ABS function. Expected to get one arguments from type NUMERIC, but got " + expCellType);
//        }
        this.exp = exp;


    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
        EffectiveValue Value = exp.calculateEffectiveValue();
        try {
            double result = Math.abs(Value.extractValueWithExpectation(Double.class));
            return new EffectiveValueImpl(CellType.NUMERIC, result);
        }
//        catch(ClassCastException e) {
//            throw new ClassCastException(e.getMessage() + "ABS function expected to get one arguments from type NUMERIC, but got " +Value.getCellType());
//        }
        catch (Exception e) {
            return new EffectiveValueImpl(CellType.NUMERIC, Double.NaN);
        }
    }
    @Override
    public  CellType getFunctionResultType(){
        return CellType.NUMERIC;
    }
}
