package parts.cell.expression.effectiveValue;

import parts.cell.CellType;

import java.io.Serializable;
import java.util.Objects;

public class EffectiveValueImpl implements EffectiveValue, Serializable {
    private CellType cellType;
    private Object value;

    public EffectiveValueImpl(CellType cellType, Object value) {
        this.cellType = cellType;
        this.value = value;
    }

    @Override
    public CellType getCellType() {
        return cellType;
    }

    @Override
    public Object getValue() {
        return value;
    }

public void setValue(Object value) {}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EffectiveValueImpl that = (EffectiveValueImpl) o;

        if (cellType != that.cellType) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = cellType != null ? cellType.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public <T> T extractValueWithExpectation(Class<T> type) {
        if (cellType.isAssignableFrom(type)) {
            return type.cast(value);
        }
        throw new ClassCastException("referenced to cell with type " + cellType + "does not match the type " );


        // error handling... exception ? return null ?

    }
}
