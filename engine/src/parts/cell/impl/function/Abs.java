package parts.cell.impl.function;

import parts.cell.EffectiveValue;
import parts.cell.EffectiveValueImpl;
import parts.cell.Expression;
import parts.cell.CellType;

public class Abs extends UnaryExpression{

    public Abs(Expression exp) {
        super(exp);
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
        EffectiveValue Value = exp.evaluate();

        double result = Math.abs(Value.extractValueWithExpectation(Double.class) );
        return  new EffectiveValueImpl(CellType.NUMERIC, result);
    }
}
