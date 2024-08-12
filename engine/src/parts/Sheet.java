package parts;

import parts.cell.Cell;

import java.util.ArrayList;

public class Sheet {
    private int version;
    private String name;
    private int rowsNum;
    private int columnsNum;
    private int columnWidth;
    private int rowHeight;
    private Cell[][] cellsMatrix; // מערך דו-ממדי של תאים


    public String getSheetName() {
        return sheetName;
    }

    public int getVersionNumber()
    {
        return versionNumber;
    }



    //print function - shouldnt be here!!!!!!!
    public void printSheetCell()
    {
        // הדפסת שמות העמודות
        System.out.print("    "); // רווח לתחילת השורה
        for (int col = 0; col < columns; col++) {
            char columnName = (char) ('A' + col);
            System.out.print(String.format("%-" + columnWidth + "s", columnName) + "|");
            //אולי לא חייב ליישר שמאלה עם האחוז מינוס
        }
        System.out.println();

        // הדפסת התאים בשורות ובעמודות
        for (int row = 0; row < rows; row++) {
            System.out.print(String.format("%02d ", row + 1)); // הדפסת מספר שורה בפורמט 01, 02, וכו'
            for (int col = 0; col < columns; col++) {
                Cell cell = cells[row][col];
                String cellValue;
                if (cell != null) {
                    cellValue = cell.getEffectiveValue();
                }
                else{
                    cellValue = "";
                }
                //String cellValue = cell != null ? cell.getEffectiveValue() : "";
                System.out.print(String.format("%-" + columnWidth + "s", cellValue) + "|");
            }
            System.out.println(); // מעבר לשורה הבאה
        }


//        //maybe change to row col
//         for(ArrayList<Cell> row : cellsMatrix)
//             for(Cell cell : row)
//                 cell.print();
    }

}
