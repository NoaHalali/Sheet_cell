package parts.sheet.cell.expression.effectiveValue;

public interface EffectiveValue {
    CellType getCellType();
    Object getValue(); //TODO - לא בטוח צריך
    <T> T extractValueWithExpectation(Class<T> type);
    @Override
    boolean equals(Object obj);
    int compare(EffectiveValue effectiveValue);
}
