import parts.Sheet;
import parts.cell.Cell;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {

        Sheet sheet = new Sheet(2,3);
        Cell[][] matrix = new Cell[2][3];
        sheet.setCellsMatrix(matrix);
        int value = 1;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j].updateEffectiveValue("3");
                value++;
            }




    }
}