package parts.cell.impl.function;

import parts.cell.CellType;
import parts.cell.EffectiveValue;
import parts.cell.Expression;

public  abstract class UnaryExpression implements Expression {
    protected Expression exp;

    //    public UnaryExpression(Expression exp) {
//        //super(originalValue);
//        this.exp = exp;
//        //this.originalValue = calculateOriginalValue();
//        this.effectiveValue = calculateEffectiveValue();
//    }
    @Override
    public abstract EffectiveValue calculateEffectiveValue();

    @Override
    public abstract CellType getFunctionResultType();

}