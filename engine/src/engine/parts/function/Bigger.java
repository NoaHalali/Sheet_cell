package engine.parts.function;

import engine.parts.sheet.cell.expression.Expression;
import engine.parts.sheet.cell.expression.effectiveValue.CellType;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValue;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValueImpl;

public class Bigger extends BinaryExpression{
    public Bigger(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
        try {
            EffectiveValue leftValue = left.calculateEffectiveValue();
            EffectiveValue rightValue = right.calculateEffectiveValue();
            boolean result = leftValue.extractValueWithExpectation(Double.class) >= rightValue.extractValueWithExpectation(Double.class);
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
