package parts.function;

import parts.cell.expression.Expression;
import parts.cell.expression.effectiveValue.CellType;
import parts.cell.expression.effectiveValue.EffectiveValue;
import parts.cell.expression.effectiveValue.EffectiveValueImpl;

public class Less extends BinaryExpression{
    public Less(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
        try {
            EffectiveValue leftValue = left.calculateEffectiveValue();
            EffectiveValue rightValue = right.calculateEffectiveValue();
            boolean result = leftValue.extractValueWithExpectation(Double.class) <= rightValue.extractValueWithExpectation(Double.class);
            return new EffectiveValueImpl(CellType.BOOLEAN, result);
//            } catch(ClassCastException e) {
//                throw new ClassCastException(e.getMessage() + "DIVIDE function expected to get two arguments from type NUMERIC, but got " +leftValue.getCellType() + " and " + rightValue.getCellType());
//            }
        }
        catch (Exception e) {
            return new EffectiveValueImpl(CellType.STRING,"UNKNOWN");//TODO איך לייצג בוליאן ?
        }
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.BOOLEAN;
    }
}
