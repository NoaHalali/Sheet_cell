package parts.cell.impl.function;

import parts.cell.Cell;
import parts.cell.CellType;
import parts.cell.EffectiveValue;
import parts.cell.Expression;


public class Ref implements Expression {
    Cell refCell;
    public Ref(Cell refCell) throws NullPointerException  {
        if(refCell == null){
            throw new NullPointerException("cannot reference an empty cell" );
        }
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
