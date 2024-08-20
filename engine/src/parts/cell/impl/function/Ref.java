package parts.cell.impl.function;

import parts.cell.Cell;
import parts.cell.EffectiveValue;
import parts.cell.impl.AbstractExpression;

public class Ref extends AbstractExpression {
    Cell refCell;
    public Ref(Cell refCell) {
        this.refCell=refCell;
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
       return refCell.geEffectiveValue();
    }
}
