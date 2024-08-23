package parts.function;

import parts.cell.expression.effectiveValue.EffectiveValue;
import parts.cell.expression.effectiveValue.EffectiveValueImpl;
import parts.cell.expression.Expression;
import parts.cell.CellType;

public class Minus extends BinaryExpression {
    public Minus(Expression expression1, Expression expression2) {
        CellType leftCellType = expression1.getFunctionResultType();
        CellType rightCellType = expression2.getFunctionResultType();
        if ( (!leftCellType.equals(CellType.NUMERIC) && !leftCellType.equals(CellType.UNKNOWN)) ||
                (!rightCellType.equals(CellType.NUMERIC) && !rightCellType.equals(CellType.UNKNOWN)) ) {
            throw new IllegalArgumentException("Invalid argument types for MINUS function. Expected NUMERIC, but got " + leftCellType + " and " + rightCellType);
        }
        left = left;
        right = right;
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
        EffectiveValue leftValue = left.calculateEffectiveValue();
        EffectiveValue rightValue=right.calculateEffectiveValue();
        double result = leftValue.extractValueWithExpectation(Double.class) - rightValue.extractValueWithExpectation(Double.class);
        return new EffectiveValueImpl(CellType.NUMERIC, result);
    }
    @Override
    public  CellType getFunctionResultType(){
        return CellType.NUMERIC;
    }
}
