package parts.cell.implementors;
import parts.cell.Calculable;

public class Bool implements Calculable {
boolean value;
    @Override
    public String evaluate() {
        return  value ? "TRUE" : "FALSE";
    }
}
