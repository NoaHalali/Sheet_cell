package parts.function;

import parts.cell.expression.effectiveValue.EffectiveValue;
import parts.cell.expression.effectiveValue.EffectiveValueImpl;
import parts.cell.expression.Expression;
import parts.cell.expression.effectiveValue.CellType;

public class Pow extends BinaryExpression{
    public Pow(Expression expression1, Expression expression2) {

        CellType leftCellType = expression1.getFunctionResultType();
        CellType rightCellType = expression2.getFunctionResultType();
        if ((!leftCellType.equals(CellType.NUMERIC) && !leftCellType.equals(CellType.UNKNOWN))){
            throw new IllegalArgumentException("Invalid argument types for POW function. Expected to get two arguments from type NUMERIC, but the first argument is " + leftCellType);
        }
        if ((!rightCellType.equals(CellType.NUMERIC) && !rightCellType.equals(CellType.UNKNOWN)) ) {
            throw new IllegalArgumentException("Invalid argument types for POW function. Expected to get two arguments from type NUMERIC, but the second argument is " + rightCellType);
        }
        left = expression1;
        right = expression2;
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
        EffectiveValue leftValue = left.calculateEffectiveValue();
        EffectiveValue rightValue=right.calculateEffectiveValue();
        try {
            double result = Math.pow(leftValue.extractValueWithExpectation(Double.class), rightValue.extractValueWithExpectation(Double.class));
            return new EffectiveValueImpl(CellType.NUMERIC, result);
        }   catch(ClassCastException e) {

            throw new ClassCastException(e.getMessage() + "POW function expected to get two arguments from type NUMERIC, but got " +leftValue.getCellType() + " and " + rightValue.getCellType());
        } catch (ArithmeticException arithmeticException) {
            return new EffectiveValueImpl(CellType.NUMERIC, Double.NaN);
        }


    }
    @Override
    public  CellType getFunctionResultType(){
        return CellType.NUMERIC;
    }
}
