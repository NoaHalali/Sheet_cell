package parts.cell.impl.function;

import parts.cell.EffectiveValue;
import parts.cell.EffectiveValueImpl;
import parts.cell.Expression;
import parts.cell.CellType;

public class Plus extends BinaryExpression {
    public Plus(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public EffectiveValue calculateEffectiveValue() {
        //TODO - add try,catch

        EffectiveValue leftValue = left.evaluate();
        EffectiveValue rightValue=right.evaluate();


       /// double leftVal = Double.parseDouble(left.evaluate().replace(",", ""));
      //  double rightVal = Double.parseDouble(right.evaluate().replace(",", ""));

        // do some checking... error handling...
        //double result = (Double) leftValue.getValue() + (Double) rightValue.getValue();
        double result = leftValue.extractValueWithExpectation(Double.class) + rightValue.extractValueWithExpectation(Double.class);
        return  new EffectiveValueImpl(CellType.NUMERIC, result);
    }
    @Override
    public  CellType getFunctionResultType(){
        return CellType.NUMERIC;
    }
}
