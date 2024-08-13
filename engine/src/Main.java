import parts.Sheet;
import parts.cell.Cell;
import parts.cell.implementors.Number;

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
                matrix[i][j].updateEffectiveValue(new Number(3));
                value++;
            }
        }
        sheet.setCellsMatrix(matrix);
        sheet.printSheetCell();
    }
}
//        Number myNumber = new Number();
//        myNumber.setNum(100060.5974);
//        String s = myNumber.evaluate();
//        System.out.println(s);





