package engine.parts.sheet.cell.expression;

import engine.parts.sheet.cell.expression.effectiveValue.CellType;
import engine.parts.sheet.cell.expression.effectiveValue.EffectiveValue;

public interface Expression {

      //returns the calculated value - Effective Value

      EffectiveValue calculateEffectiveValue();
      CellType getFunctionResultType();

       //הגדרת ערך חדש כערך המקורי (מעודכן גם את ה-Evaluate)

}