package parts.cell.impl.function;

import parts.cell.Cell;
import parts.cell.EffectiveValue;
import parts.cell.Expression;

public class Ref extends UnaryExpression {
    public Ref(Expression exp) {
        super(exp);
    }

    @Override
    protected EffectiveValue calculateEffectiveValue() {
        //get
        return null;
    }
}
