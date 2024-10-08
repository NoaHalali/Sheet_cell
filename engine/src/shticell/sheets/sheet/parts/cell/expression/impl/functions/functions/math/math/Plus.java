package shticell.sheets.sheet.parts.cell.expression.impl.functions.functions.math;

import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValueImpl;
import shticell.sheets.sheet.parts.cell.expression.Expression;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.CellType;
import shticell.sheets.sheet.parts.cell.expression.impl.functions.BinaryExpression;

public class Plus extends BinaryExpression {
    public Plus(Expression expression1, Expression expression2) {
//        CellType leftCellType = expression1.getFunctionResultType();
//        CellType rightCellType = expression2.getFunctionResultType();
//        if ((!leftCellType.equals(CellType.NUMERIC) && !leftCellType.equals(CellType.UNKNOWN))){
//            throw new IllegalArgumentException("Invalid argument types for PLUS function. Expected to get two arguments from type NUMERIC, but the first argument is " + leftCellType);
//        }
//        if ((!rightCellType.equals(CellType.NUMERIC) && !rightCellType.equals(CellType.UNKNOWN)) ) {
//            throw new IllegalArgumentException("Invalid argument types for PLUS function. Expected to get two arguments from type NUMERIC, but the second argument is " + rightCellType);
//        }
        left = expression1;
        right = expression2;
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {

        EffectiveValue leftValue = left.calculateEffectiveValue();
        EffectiveValue rightValue = right.calculateEffectiveValue();

        // do some checking... error handling...
        //double result = (Double) leftValue.getValue() + (Double) rightValue.getValue();
        try {
            double result = leftValue.extractValueWithExpectation(Double.class) + rightValue.extractValueWithExpectation(Double.class);
            return new EffectiveValueImpl(CellType.NUMERIC, result);
             
        }
//        catch(ClassCastException e) {
//
//            throw new ClassCastException(e.getMessage() + "PLUS function expected to get two arguments from type NUMERIC, but got " +leftValue.getCellType() + " and " + rightValue.getCellType());
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
