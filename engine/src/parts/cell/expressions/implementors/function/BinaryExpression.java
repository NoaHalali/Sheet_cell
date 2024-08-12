package parts.cell.expressions.implementors.function;

public abstract class BinaryExpression extends Function{
    private Expression expression1;
    private Expression expression2;


    public BinaryExpression(Expression expression1, Expression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public String evaluate() {
        return String.valueOf(evaluate(expression1.evaluate(), expression2.evaluate()));
    }

    @Override
    public String toString() {
        return "(" + expression1 + getOperationSign() + expression2 + ")";
    }

    abstract protected double evaluate(double evaluate, double evaluate2);
}
