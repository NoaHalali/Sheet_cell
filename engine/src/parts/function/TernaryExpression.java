package parts.function;

import parts.cell.CellType;
import parts.cell.expression.effectiveValue.EffectiveValue;
import parts.cell.expression.Expression;

public abstract class TernaryExpression implements Expression {
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
