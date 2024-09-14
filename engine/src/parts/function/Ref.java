package parts.function;

import parts.sheet.cell.Cell;
import parts.sheet.cell.expression.effectiveValue.CellType;
import parts.sheet.cell.expression.effectiveValue.EffectiveValue;
import parts.sheet.cell.expression.Expression;

import java.io.Serializable;


public class Ref implements Expression, Serializable {
    Cell refCell;
    public Ref(Cell refCell) throws NullPointerException  {
//        if(refCell == null){
//           // throw new NullPointerException("cannot reference an empty cell" );
//        }
        this.refCell = refCell;
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
       return refCell.getEffectiveValue();
    }
    @Override
    public CellType getFunctionResultType(){
       return CellType.UNKNOWN;

    }
}
