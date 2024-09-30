package shticell.sheets.sheet.parts.cell.expression.impl.functions.functions.logic;

import shticell.sheets.sheet.parts.cell.expression.Expression;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.CellType;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValueImpl;
import shticell.sheets.sheet.parts.cell.expression.impl.functions.functions.UnaryExpression;

public class Not extends UnaryExpression {
    public Not(Expression exp) {

        this.exp = exp;


    }
    @Override
    public EffectiveValue calculateEffectiveValue() {
        EffectiveValue Value = exp.calculateEffectiveValue();
        try {
            boolean result = !Value.extractValueWithExpectation(Boolean.class);
            return new EffectiveValueImpl(CellType.BOOLEAN, result);
        }
//        catch(ClassCastException e) {
//            throw new ClassCastException(e.getMessage() + "ABS function expected to get one arguments from type NUMERIC, but got " +Value.getCellType());
//        }
        catch (Exception e) {
            return new EffectiveValueImpl(CellType.STRING,"UNKNOWN");
        }
    }

    @Override
    public CellType getFunctionResultType() {
        return null;
    }
}
