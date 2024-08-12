package parts.cell.implementors;
import parts.cell.Calculable;

public class Number implements Calculable {

    private double num;

    @Override
    public String evaluate() { //אולי לא מחזיר string? לחשוב על זה
        //String res;
        double temp = num, mod;
        StringBuilder str = new StringBuilder();

        while (temp > 0) {
            temp = temp / 1000;
            mod = temp % 1000;
            str.insert(0, "," + mod);
        }
        mod = temp % 1000;
        str.insert(0, mod);

        return String.valueOf(str); //הצעה של הקומפיילר
    }
}
