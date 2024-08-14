package parts.cell.impl.function;

public class Sub extends TernaryExpression{
    @Override
    protected String calculateEffectiveValue() {
        String st = exp1.evaluate();
        double oneIndex  = Double.parseDouble(exp2.evaluate().replace(",", ""));
        double secondIndex = Double.parseDouble(exp3.evaluate().replace(",", ""));
        return st.substring((int)oneIndex,(int)secondIndex);
    }
}
