package parts.cell.implementors;

import parts.cell.Calculable;
import java.text.DecimalFormat;

public class Number implements Calculable {

    private double num;
    public Number(double num) {
        this.num = num;
    }

    @Override
    public String evaluate() {
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
