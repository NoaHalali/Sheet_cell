package parts.cell.impl;

import parts.cell.Expression;
import java.text.DecimalFormat;

public class Number implements Expression {

    private double num;

    public Number(double num) {
        this.num = num;
    }
    public double getValue()
    {
        return num;
    }

    @Override
    public Expression evaluate() {
        DecimalFormat decimalFormat;

        // בדיקה אם המספר שלם או ממשי
        if (num == (long) num) {
            decimalFormat = new DecimalFormat("#,###");
        } else {
            decimalFormat = new DecimalFormat("#,###.00");
        }

        return decimalFormat.format(num);
    }
}
