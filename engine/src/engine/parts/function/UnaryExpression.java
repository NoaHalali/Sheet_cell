package engine.parts.function;

import engine.parts.sheet.cell.expression.effectiveValue.CellType;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValue;
import engine.parts.sheet.cell.expression.Expression;

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