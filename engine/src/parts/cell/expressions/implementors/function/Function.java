package parts.cell.expressions.implementors.function;

import parts.cell.expressions.Expression;

public abstract class Function implements Expression {


    @Override
    public String evaluate() {
        return "";
    }

    abstract protected double evaluate(double evaluate, double evaluate2);

}
