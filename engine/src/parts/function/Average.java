package parts.function;

import parts.Range;
import parts.cell.expression.Expression;

public class Average implements Expression {
    private Range range;

    public Average(Expression[] expressions) {
        this.expressions = expressions;
    }

    @Override
    public double evaluate() {
        double sum = 0;
        for (Expression expression : expressions) {
            sum += expression.evaluate();
        }
        return sum / expressions.length;
    }
}
