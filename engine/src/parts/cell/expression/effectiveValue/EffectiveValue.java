package parts.cell.expression.effectiveValue;

import parts.cell.CellType;

public interface EffectiveValue {
    CellType getCellType();
    Object getValue(); //TODO - לא בטוח צריך
    <T> T extractValueWithExpectation(Class<T> type);
}
