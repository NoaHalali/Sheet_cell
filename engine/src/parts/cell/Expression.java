package parts.cell;

public interface Expression {

      //returns the calculated value - Effective Value

      EffectiveValue calculateEffectiveValue();
      CellType getFunctionResultType();

       //הגדרת ערך חדש כערך המקורי (מעודכן גם את ה-Evaluate)

}