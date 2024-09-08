package parts.function;

import parts.cell.expression.effectiveValue.EffectiveValue;
import parts.cell.expression.effectiveValue.EffectiveValueImpl;
import parts.cell.expression.Expression;
import parts.cell.expression.effectiveValue.CellType;

public class Concat extends BinaryExpression{
    public Concat(Expression left, Expression right) {
//        CellType leftCellType = left.getFunctionResultType();
//        CellType rightCellType = right.getFunctionResultType();
//        if ((!leftCellType.equals(CellType.STRING) && !leftCellType.equals(CellType.UNKNOWN))){
//            throw new IllegalArgumentException("Invalid argument types for CONCAT function. Expected to get two arguments from type STRING, but the first argument is " + leftCellType);
//        }
//        if ((!rightCellType.equals(CellType.STRING) && !rightCellType.equals(CellType.UNKNOWN)) ) {
//            throw new IllegalArgumentException("Invalid argument types for CONCAT function. Expected to get two arguments from type STRING, but the second argument is " + rightCellType);
//        }
      this.left = left;
      this.right = right;
    }

    //TODO FIX IMPLEMENT WAIT FOR AVIAD RESPONSE
    @Override
    public EffectiveValue calculateEffectiveValue() {
        EffectiveValue leftValue = left.calculateEffectiveValue();
        EffectiveValue rightValue = right.calculateEffectiveValue();
        try {

            // בדיקה אם אחת מהמחרוזות היא "UNDEFINED"
            String leftStr = leftValue.extractValueWithExpectation(String.class);
            String rightStr = rightValue.extractValueWithExpectation(String.class);

            if ("UNDEFINED".equals(leftStr) || "UNDEFINED".equals(rightStr)) {
                return new EffectiveValueImpl(CellType.STRING, "UNDEFINED");
            }
            if ("UNKNOWN".equals(leftStr) || "UNKNOWN".equals(rightStr)) {
                return new EffectiveValueImpl(CellType.STRING, "UNDEFINED");
            }


            // ביצוע הקונקט (שרשור המחרוזות)
            String result = leftStr + rightStr;
            return new EffectiveValueImpl(CellType.STRING, result);
        }
//        catch(ClassCastException e) {
//            throw new ClassCastException(e.getMessage() + "CONCAT function expected to get two arguments from type STRING, but got " +leftValue.getCellType() + " and " + rightValue.getCellType());
//        }
        catch (Exception e) {
            // במקרה של חריגה כלשהי, מחזירים "UNDEFINED"
            return new EffectiveValueImpl(CellType.STRING, "UNDEFINED");
        }
    }

    @Override
    public  CellType getFunctionResultType(){
        return CellType.STRING;
    }
}
