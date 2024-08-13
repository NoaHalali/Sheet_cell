package parts.cell.impl;
import parts.cell.Expression;

public class Bool implements Expression {
boolean value;
    @Override
    public Expression evaluate() {
        return  value ? "TRUE" : "FALSE";
    }
}
