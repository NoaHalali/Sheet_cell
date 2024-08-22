package parts.cell.impl.function;

import parts.cell.EffectiveValue;
import parts.cell.EffectiveValueImpl;
import parts.cell.Expression;
import parts.cell.CellType;

public class Sub extends TernaryExpression{
    public Sub(Expression exp1, Expression exp2, Expression exp3) {
        CellType exp1CellType = exp1.getFunctionResultType();
        CellType exp2CellType = exp2.getFunctionResultType();
        CellType exp3CellType = exp3.getFunctionResultType();

        if ( (!exp1CellType.equals(CellType.NUMERIC) && !exp1CellType.equals(CellType.UNKNOWN)) ||
                (!exp2CellType.equals(CellType.NUMERIC) && !exp2CellType.equals(CellType.UNKNOWN)) ||
                (!exp3CellType.equals(CellType.NUMERIC) && !exp3CellType.equals(CellType.UNKNOWN)))
          {
            throw new IllegalArgumentException("Invalid argument types for POW function. Expected STRING, NUMERIC, NUMERIC, but got " + exp1CellType + " , " + exp2CellType + " and " + exp3CellType);
          }
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3=exp3;
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
        EffectiveValue mainValue = exp1.calculateEffectiveValue();
        EffectiveValue index1Val = exp2.calculateEffectiveValue();
        EffectiveValue index2Val = exp3.calculateEffectiveValue();
        String str = mainValue.extractValueWithExpectation(String.class);
        int firstIndex = index1Val.extractValueWithExpectation(Double.class).intValue();//לוודא שבאמת int
        int secondIndex = index2Val.extractValueWithExpectation(Double.class).intValue();
        String  result = str.substring(firstIndex,secondIndex);
        return new EffectiveValueImpl(CellType.STRING, result);
    }
    @Override
    public CellType getFunctionResultType(){
        return CellType.STRING;
    }
}
