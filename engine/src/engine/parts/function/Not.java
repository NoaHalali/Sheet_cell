package engine.parts.function;

import engine.parts.sheet.cell.expression.Expression;
import engine.parts.sheet.cell.expression.effectiveValue.CellType;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValue;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValueImpl;

public class Not extends UnaryExpression{
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
