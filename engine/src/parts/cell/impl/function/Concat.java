package parts.cell.impl.function;

import parts.cell.EffectiveValue;
import parts.cell.EffectiveValueImpl;
import parts.cell.Expression;
import parts.cell.CellType;

public class Concat extends BinaryExpression{
    public Concat(Expression left, Expression right) {
        super(left, right);
    }

    //TODO FIX IMPLEMENT WAIT FOR AVIAD RESPONSE
    @Override
    public EffectiveValue calculateEffectiveValue() {
        EffectiveValue leftValue = left.evaluate();
        EffectiveValue rightValue=right.evaluate();
        String LeftStr = leftValue.extractValueWithExpectation(String.class);
        String RightStr = rightValue.extractValueWithExpectation(String.class);
        String Res = LeftStr + RightStr;
        return new EffectiveValueImpl(CellType.STRING,Res) ;
    }
}
