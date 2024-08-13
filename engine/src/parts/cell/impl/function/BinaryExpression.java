package parts.cell.impl.function;

import parts.cell.Expression;

import java.security.PublicKey;

public abstract class BinaryExpression implements Expression {
    protected Expression left;
    protected Expression right;
    private String operator;
    private String originalValue;
    private String effectiveValue;

    public BinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        this.operator = operator;
        //this.originalValue = calculateOriginalValue();
        this.effectiveValue = calculateEffectiveValue();
    }
    protected abstract String calculateOriginalValue();

    // חישוב הערך המקורי (בדוגמה הזו הוא מבוסס על הביטוי המקורי)
//    private String calculateOriginalValue() {
//        return left.evaluate() + " " + operator + " " + right.evaluate();
//    }

    // חישוב הערך המוערך
    //אולי נממש במחלקות של הפעולות עצמן
//    private String calculateEffectiveValue() {
//        double leftVal = Double.parseDouble(left.evaluate().replace(",", ""));
//        double rightVal = Double.parseDouble(right.evaluate().replace(",", ""));
//        switch (operator) {
//            case "+":
//                return String.valueOf(leftVal + rightVal);
//            case "-":
//                return String.valueOf(leftVal - rightVal);
//            // הוספה של פעולות נוספות לפי הצורך
//            default:
//                throw new UnsupportedOperationException("Unsupported operator: " + operator);
//        }
//    }

    @Override
    public String evaluate() {
        return effectiveValue; // מחזיר את הערך המוערך שכבר חושב
    } //אולי להוסיף בוליאני שאומר אם מעודכן

    public String getOriginalValue() {
        return originalValue;
    }

//    public String getEffectiveValue() {
//        return effectiveValue;
//    }
}

//public abstract class BinaryExpression implements Expression {
//
//    private Expression expression1;
//    private Expression expression2;
//
//    public BinaryExpression(Expression expression1, Expression expression2) {
//        this.expression1 = expression1;
//        this.expression2 = expression2;
//    }
//
//    @Override
//    public Expression evaluate() {
//        return evaluate(expression1.evaluate(), expression2.evaluate());
//    }
//
//    abstract public Expression evaluate(Expression arg1, Expression arg2);
//
////    @Override
////    public String toString() {
////        return "(" + expression1 + getOperationSign() + expression2 + ")";
////    }
//
//}

