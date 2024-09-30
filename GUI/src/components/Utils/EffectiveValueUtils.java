package components.Utils;

import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;

public class EffectiveValueUtils {
    public static String calcValueToString(EffectiveValue effectiveValue) {
        if (effectiveValue == null) {
            return "";
        }
        switch (effectiveValue.getCellType()) {
            case NUMERIC:
                double num = effectiveValue.extractValueWithExpectation(Double.class);
                if (Double.isNaN(num) || Double.isInfinite(num)) return "NaN";
                return num == Math.floor(num) ? String.format("%,d", (long) num) : String.format("%,.2f", num);
            case STRING:
                return effectiveValue.extractValueWithExpectation(String.class).trim();
            case BOOLEAN:
                return String.valueOf(effectiveValue.extractValueWithExpectation(Boolean.class)).toUpperCase();
            default:
                throw new IllegalArgumentException();
        }
    }
}
