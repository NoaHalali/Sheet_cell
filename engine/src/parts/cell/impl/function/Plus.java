package parts.cell.impl.function;

import com.sun.codemodel.JCatchBlock;
import parts.cell.EffectiveValue;
import parts.cell.EffectiveValueImpl;
import parts.cell.Expression;
import parts.cell.CellType;

public class Plus extends BinaryExpression {
    public Plus(Expression expression1, Expression expression2) {
        CellType leftCellType = left.getFunctionResultType();
        CellType rightCellType = right.getFunctionResultType();
        if ( (!leftCellType.equals(CellType.NUMERIC) && !leftCellType.equals(CellType.UNKNOWN)) ||
                (!rightCellType.equals(CellType.NUMERIC) && !rightCellType.equals(CellType.UNKNOWN)) ) {
            throw new IllegalArgumentException("Invalid argument types for PLUS function. Expected NUMERIC, but got " + leftCellType + " and " + rightCellType);
        }
        left = left;
        right = right;
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
        //TODO - add try,catch

        EffectiveValue leftValue = left.evaluate();
        EffectiveValue rightValue = right.evaluate();
        //double result=null;

       /// double leftVal = Double.parseDouble(left.evaluate().replace(",", ""));
      //  double rightVal = Double.parseDouble(right.evaluate().replace(",", ""));

        // do some checking... error handling...
        //double result = (Double) leftValue.getValue() + (Double) rightValue.getValue();
        try {
            double result = leftValue.extractValueWithExpectation(Double.class) + rightValue.extractValueWithExpectation(Double.class);
            return  new EffectiveValueImpl(CellType.NUMERIC, result);
             
        }
        catch(ClassCastException e) {
            throw new ClassCastException(e.getMessage() +"for PLUS function. Expected NUMERIC, but got" +leftValue.getCellType() + " and " + rightValue.getCellType());
        }
    }
    @Override
    public  CellType getFunctionResultType(){
        return CellType.NUMERIC;
    }
}
