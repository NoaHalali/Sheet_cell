package parts.cell.impl.function;

import parts.cell.EffectiveValue;
import parts.cell.EffectiveValueImpl;
import parts.cell.Expression;
import parts.cell.CellType;

public class Sub extends TernaryExpression{
    public Sub(Expression exp1, Expression exp2, Expression exp3) {
        super(exp1, exp2, exp3);
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
        EffectiveValue mainValue = exp1.evaluate();
        EffectiveValue index1Val = exp2.evaluate();
        EffectiveValue index2Val = exp3.evaluate();
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
