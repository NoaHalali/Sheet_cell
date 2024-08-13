import parts.Sheet;
import parts.cell.Cell;
import parts.cell.Expression;
import parts.cell.impl.NumberExpression;
import parts.cell.impl.function.Plus;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {

        Sheet sheet = new Sheet(2, 3);
        Cell[][] matrix = new Cell[2][3];

        int value = 1;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                matrix[i][j]=new Cell();
                matrix[i][j].updateValue(String.valueOf(value));
                value++;
            }
        }

        sheet.setCellsMatrix(matrix);
        sheet.printSheetCell();

//        Expression i=new Plus(new Plus(new Number(7),new Number(5)),new Plus(new Number(1),new Number(3)));
//        String t=   i.evaluate();
//        System.out.println(t);
    }
}






