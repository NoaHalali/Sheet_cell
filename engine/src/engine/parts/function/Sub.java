package engine.parts.function;

import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValue;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValueImpl;
import engine.parts.sheet.cell.expression.Expression;
import engine.parts.sheet.cell.expression.effectiveValue.CellType;

public class Sub extends TernaryExpression{
    public Sub(Expression expression1, Expression expression2, Expression expression3) {
//        CellType exp1CellType = expression1.getFunctionResultType();
//        CellType exp2CellType = expression2.getFunctionResultType();
//        CellType exp3CellType = expression3.getFunctionResultType();
//        if ((!exp1CellType.equals(CellType.STRING) && !exp1CellType.equals(CellType.UNKNOWN))){
//            throw new IllegalArgumentException("Invalid argument types for SUB function. expected to get three arguments, the first argument should be from type STRING, and the other two arguments should be from type NUMERIC, but the first argument is " + exp1CellType);
//        }
//        if ((!exp2CellType.equals(CellType.NUMERIC) && !exp2CellType.equals(CellType.UNKNOWN))){
//            throw new IllegalArgumentException("Invalid argument types for SUB function. expected to get three arguments, the first argument should be from type STRING, and the other two arguments should be from type NUMERIC, but the second argument is " + exp2CellType);
//        }
//        if ((!exp3CellType.equals(CellType.NUMERIC) && !exp3CellType.equals(CellType.UNKNOWN))){
//            throw new IllegalArgumentException("Invalid argument types for SUB function. expected to get three arguments, the first argument should be from type STRING, and the other two arguments should be from type NUMERIC, but the third argument is " + exp3CellType);
//        }
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
        EffectiveValue mainValue = expression1.calculateEffectiveValue();
        EffectiveValue index1Val = expression2.calculateEffectiveValue();
        EffectiveValue index2Val = expression3.calculateEffectiveValue();

        try {
            String str = mainValue.extractValueWithExpectation(String.class);
            if ("UNDEFINED".equals(str)) {
                return new EffectiveValueImpl(CellType.STRING, "UNDEFINED");
            }
            if ("UNKNOWN".equals(str)) {
                return new EffectiveValueImpl(CellType.STRING, "UNDEFINED");
            }

            // חישוב האינדקסים
            int firstIndex = index1Val.extractValueWithExpectation(Double.class).intValue();
            int secondIndex = index2Val.extractValueWithExpectation(Double.class).intValue();

            if (firstIndex < 0 || secondIndex < 0 || firstIndex >= str.length() || secondIndex >= str.length() || firstIndex > secondIndex) {
                return new EffectiveValueImpl(CellType.STRING, "UNDEFINED");
            }

            String result = str.substring(firstIndex, secondIndex + 1);

            return new EffectiveValueImpl(CellType.STRING, result);
        }
//        catch(ClassCastException e) {
//            throw new ClassCastException(e.getMessage() + "SUB function expected to receive STRING, NUMERIC, NUMERIC, but got " +mainValue.getCellType()+", " +index1Val.getCellType()+ " and " + index2Val.getCellType());
//        }
        catch (Exception e) {
            return new EffectiveValueImpl(CellType.STRING, "UNDEFINED");
        }
    }

    @Override
    public CellType getFunctionResultType(){
        return CellType.STRING;
    }
}