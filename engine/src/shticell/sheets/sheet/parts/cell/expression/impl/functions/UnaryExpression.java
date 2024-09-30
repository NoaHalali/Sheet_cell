package shticell.sheets.sheet.parts.cell.expression.impl.functions.functions;

import shticell.sheets.sheet.parts.cell.expression.effectiveValue.CellType;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import shticell.sheets.sheet.parts.cell.expression.Expression;

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