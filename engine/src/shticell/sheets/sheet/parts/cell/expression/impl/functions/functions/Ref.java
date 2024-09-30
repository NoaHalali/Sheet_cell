package shticell.sheets.sheet.parts.cell.expression.impl.functions.functions;

import shticell.sheets.sheet.parts.cell.Cell;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.CellType;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;
import shticell.sheets.sheet.parts.cell.expression.Expression;

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
