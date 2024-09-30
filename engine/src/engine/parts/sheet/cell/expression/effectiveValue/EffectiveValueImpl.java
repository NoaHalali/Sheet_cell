package engine.parts.sheet.cell.expression.effectiveValue;

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
        throw new ClassCastException("referenced to cell with type " + cellType + ". ");


        // error handling... exception ? return null ?

    }
    @Override
    public int compare(EffectiveValue effectiveValue) {
        if (cellType == CellType.NUMERIC && effectiveValue.getCellType() == CellType.NUMERIC) {
         double val1 = (double) effectiveValue.getValue();
         double val2 = (double) effectiveValue.getValue();
         return Double.compare(val1, val2);

        }
//        else if(cellType == CellType.NUMERIC && effectiveValue.getCellType() != CellType.NUMERIC){//the first is number and the second is
//         return 1;
//
//        }
//        else if(cellType != CellType.NUMERIC && effectiveValue.getCellType() == CellType.NUMERIC){//first not number but second is
//            return  -1;
//
//        }else{//both not numbers
//            return 0;
//        }
        return 0;
    }


}
