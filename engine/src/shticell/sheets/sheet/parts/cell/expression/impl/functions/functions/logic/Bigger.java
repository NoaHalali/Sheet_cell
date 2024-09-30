package shticell.sheets.sheet.parts.cell.expression.impl.functions.functions.logic;

import shticell.sheets.sheet.parts.cell.expression.Expression;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.CellType;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValueImpl;
import shticell.sheets.sheet.parts.cell.expression.impl.functions.BinaryExpression;

public class Bigger extends BinaryExpression {
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
