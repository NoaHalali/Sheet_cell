package parts.cell.implementors;
import parts.cell.Expression;

public class StringVal implements Expression {

    private String value;
    @Override
    public String evaluate() {
        return value.trim();
    }
}
