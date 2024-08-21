package parts.cell.impl.function;

import parts.cell.Cell;
import parts.cell.CellType;
import parts.cell.EffectiveValue;
import parts.cell.impl.AbstractExpression;

public class Ref extends AbstractExpression {
    Cell refCell;
    public Ref(Cell refCell) throws NullPointerException  {
        if(refCell == null){
            throw new NullPointerException("cannot reference an empty cell" );
        }
        this.refCell = refCell;
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
       return refCell.geEffectiveValue();
    }
    @Override
    public CellType getFunctionResultType(){
       return CellType.UNKNOWN;

    }
}
