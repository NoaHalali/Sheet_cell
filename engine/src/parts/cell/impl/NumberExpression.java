package parts.cell.impl;

import java.text.DecimalFormat;

public class NumberExpression extends AbstractExpression {
    private double num;

    //or get String and then Parse to double
    public NumberExpression(double num) {
        super(Double.toString(num));
        this.num = num;
    }

    @Override
    protected String calculateEffectiveValue() {
        DecimalFormat decimalFormat;

        if (num == (long) num) {
            decimalFormat = new DecimalFormat("#,###");
        } else {
            decimalFormat = new DecimalFormat("#,###.00");
        }
        return decimalFormat.format(num);
    }
}
