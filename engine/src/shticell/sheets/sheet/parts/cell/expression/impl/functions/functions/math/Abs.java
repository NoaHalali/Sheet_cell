package shticell.sheets.sheet.parts.cell.expression.impl.functions.functions.math;

import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValueImpl;
import shticell.sheets.sheet.parts.cell.expression.Expression;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.CellType;
import shticell.sheets.sheet.parts.cell.expression.impl.functions.functions.UnaryExpression;

public class Abs extends UnaryExpression {

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
