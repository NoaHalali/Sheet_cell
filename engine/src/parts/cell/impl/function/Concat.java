package parts.cell.impl.function;

import parts.cell.EffectiveValue;
import parts.cell.EffectiveValueImpl;
import parts.cell.Expression;
import parts.cell.CellType;

public class Concat extends BinaryExpression{
    public Concat(Expression left, Expression right) {
        CellType leftCellType = left.getFunctionResultType();
        CellType rightCellType = right.getFunctionResultType();
        if ( (!leftCellType.equals(CellType.NUMERIC) && !leftCellType.equals(CellType.UNKNOWN)) ||
                (!rightCellType.equals(CellType.NUMERIC) && !rightCellType.equals(CellType.UNKNOWN)) ) {
            throw new IllegalArgumentException("Invalid argument types for CONCAT function. Expected STRING, but got " + leftCellType + " and " + rightCellType);
        }
      this.left = left;
      this.right = right;
    }

    //TODO FIX IMPLEMENT WAIT FOR AVIAD RESPONSE
    @Override
    public EffectiveValue calculateEffectiveValue() {
        EffectiveValue leftValue = left.calculateEffectiveValue();
        EffectiveValue rightValue=right.calculateEffectiveValue();
        String LeftStr = leftValue.extractValueWithExpectation(String.class);
        String RightStr = rightValue.extractValueWithExpectation(String.class);
        String Res = LeftStr + RightStr;
        return new EffectiveValueImpl(CellType.STRING,Res) ;
    }
    @Override
    public  CellType getFunctionResultType(){
        return CellType.STRING;
    }
}
