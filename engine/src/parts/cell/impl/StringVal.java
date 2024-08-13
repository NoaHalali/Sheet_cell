package parts.cell.impl;
import parts.cell.Expression;

public class StringVal implements Expression {

    private String value;
    @Override
    public Expression evaluate() {
        return value.trim();
    }
}
