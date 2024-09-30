package engine.parts.function;

import engine.parts.sheet.cell.expression.effectiveValue.CellType;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValue;
import engine.parts.sheet.cell.expression.Expression;

import java.io.Serializable;

public abstract class TernaryExpression implements Expression, Serializable {
    protected Expression expression1;
    protected Expression expression2;
    protected Expression expression3;
//    public TernaryExpression(Expression exp1,Expression exp2,Expression exp3) {
//        //super(originalValue);
//       this.exp1 = exp1;
//       this.exp2 = exp2;
//       this.exp3 = exp3;
//        //this.originalValue = calculateOriginalValue();
//        this.effectiveValue = calculateEffectiveValue();
//    }
    @Override
    public abstract EffectiveValue calculateEffectiveValue();
    @Override
    public abstract CellType getFunctionResultType();
}