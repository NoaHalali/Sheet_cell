package parts.cell.expressions.implementors.function;

public enum Operation extends BinaryExpression {


    PLUS{
        @Override
        public String evaluate() {
            return "";}
    },
    MINUS
            {
                @Override
                public String evaluate() {
                    return "";
                }
            }
}
