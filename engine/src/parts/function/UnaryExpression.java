package parts.function;

import parts.cell.CellType;
import parts.cell.expression.effectiveValue.EffectiveValue;
import parts.cell.expression.Expression;

import java.io.Serializable;

public  abstract class UnaryExpression implements Expression, Serializable {
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