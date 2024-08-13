package parts.cell.implementors;
import parts.cell.Expression;

public class Bool implements Expression {
boolean value;
    @Override
    public String evaluate() {
        return  value ? "TRUE" : "FALSE";
    }
}
