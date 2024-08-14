package parts.cell.impl.function;

public class Abs extends UnaryExpression{


    @Override
    protected String calculateEffectiveValue() {
        double val = Double.parseDouble(exp.evaluate().replace(",", ""));
        return String.valueOf(Math.abs(val));
    }
}
