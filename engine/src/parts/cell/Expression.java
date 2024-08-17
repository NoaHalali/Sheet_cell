package parts.cell;

public interface Expression {

      //returns the calculated value - Effective Value
      EffectiveValue evaluate();
      String getOriginalValue();


       //הגדרת ערך חדש כערך המקורי (מעודכן גם את ה-Evaluate)
      void setOriginalValue(Expression originalValue);
}