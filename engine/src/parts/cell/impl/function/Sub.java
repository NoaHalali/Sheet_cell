package parts.cell.impl.function;

import parts.cell.EffectiveValue;
import parts.cell.EffectiveValueImpl;
import parts.cell.impl.CellType;

public class Sub extends TernaryExpression{
    @Override
    protected EffectiveValue calculateEffectiveValue() {
        EffectiveValue mainValue = exp1.evaluate();
        EffectiveValue index1Val = exp2.evaluate();
        EffectiveValue index2Val = exp3.evaluate();
        String str = mainValue.extractValueWithExpectation(String.class);
        int firstIndex=index1Val.extractValueWithExpectation(Double.class).intValue();//לוודא שבאמת int
        int secondIndex=index2Val.extractValueWithExpectation(Double.class).intValue();
        String  result = str.substring(firstIndex,secondIndex);
        return new EffectiveValueImpl(CellType.STRING, result);
    }
}
