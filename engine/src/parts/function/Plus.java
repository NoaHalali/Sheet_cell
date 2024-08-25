package parts.function;

import parts.cell.expression.effectiveValue.EffectiveValue;
import parts.cell.expression.effectiveValue.EffectiveValueImpl;
import parts.cell.expression.Expression;
import parts.cell.expression.effectiveValue.CellType;

public class Plus extends BinaryExpression {
    public Plus(Expression expression1, Expression expression2) {
        CellType leftCellType = expression1.getFunctionResultType();
        CellType rightCellType = expression2.getFunctionResultType();
        if ( (!leftCellType.equals(CellType.NUMERIC) && !leftCellType.equals(CellType.UNKNOWN)) ||
                (!rightCellType.equals(CellType.NUMERIC) && !rightCellType.equals(CellType.UNKNOWN)) ) {
            throw new IllegalArgumentException("Invalid argument types for PLUS function. Expected NUMERIC, but got " + leftCellType + " and " + rightCellType);
        }
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
            return  new EffectiveValueImpl(CellType.NUMERIC, result);
             
        }
        catch(ClassCastException e) {

            throw new ClassCastException(e.getMessage() + "PLUS function expected to receive NUMERIC, but got " +leftValue.getCellType() + " and " + rightValue.getCellType());
        }
    }
    @Override
    public  CellType getFunctionResultType(){
        return CellType.NUMERIC;
    }
}
