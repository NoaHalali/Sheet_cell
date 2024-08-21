package parts.cell;

public class EffectiveValueImpl implements EffectiveValue {
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
    public <T> T extractValueWithExpectation(Class<T> type) {
        if (cellType.isAssignableFrom(type)) {
            return type.cast(value);
        }
        throw new ClassCastException("referenced to cell with type " + cellType + "does not match the type" + type);


        // error handling... exception ? return null ?

    }
}
