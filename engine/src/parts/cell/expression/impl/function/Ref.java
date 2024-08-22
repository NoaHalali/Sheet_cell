package parts.cell.expression.impl.function;

import parts.cell.Cell;
import parts.cell.CellType;
import parts.cell.expression.effectiveValue.EffectiveValue;
import parts.cell.expression.Expression;


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
