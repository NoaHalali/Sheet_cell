package parts;

import parts.cell.Cell;

public class Sheet {
    private int version;
    private String name;
    private int numberOfRows;
    private int numberOfCols;
    private int columnWidth;
    private int rowHeight;
    private Cell[][] cellsMatrix; // מערך דו-ממדי של תאים

    public Sheet(int numberOfRows, int numberOfCols) {
        this.numberOfRows = numberOfRows;
        this.numberOfCols = numberOfCols;
    }

    public String getSheetName() {
        return name;
    }
    //change1
    public int getVersionNumber()
    {
        return version;
    }

    //print function - shouldnt be here!!!!!!!
    public void printSheetCell()
    {
        // הדפסת שמות העמודות
        System.out.print("    "); // רווח לתחילת השורה
        for (int col = 0; col < numberOfCols; col++) {
            char columnName = (char) ('A' + col);
            System.out.print(String.format("%-" + columnWidth + "s", columnName) + "|");
            //אולי לא חייב ליישר שמאלה עם האחוז מינוס
        }
        System.out.println();

        // הדפסת שמות העמודות
        System.out.print("    "); // רווח לתחילת השורה
        for (int col = 0; col < numberOfCols; col++) {
            char columnName = (char) ('A' + col);
            System.out.print(String.format("%-" + columnWidth + "s", columnName) + "|");
        }
        System.out.println();

        // הדפסת התאים בשורות ובעמודות
        for (int row = 0; row < numberOfRows; row++) {
            System.out.print(String.format("%02d ", row + 1)); // הדפסת מספר שורה בפורמט 01, 02, וכו'
            for (int col = 0; col < numberOfCols; col++) {
                Cell cell = cellsMatrix[row][col];
                String cellValue = cell != null ? cell.geEffectiveValue() : "";
                // התאמה ל-rowHeight: חותכים את התוכן אם הוא ארוך מדי, או מוסיפים רווחים אם הוא קצר
                String[] lines = formatCellContent(cellValue, columnWidth, rowHeight);
                for (String line : lines) {
                    System.out.print(line + "|");
                }
                System.out.println(); // מעבר לשורה הבאה עבור שורות נוספות בתא
            }
        }
    }
    public void setCellsMatrix(Cell[][] cellsMatrix) {
        this.cellsMatrix = cellsMatrix;
    }

    //אם היא במחלקה הזו אז לא צריך לשלוח את השורות והעמודות לפונקציה
    private String[] formatCellContent(String content, int columnWidth, int rowHeight) {
        String[] lines = new String[rowHeight];
        String formattedContent = String.format("%-" + columnWidth + "s", content);
        for (int i = 0; i < rowHeight; i++) {
            if (i < formattedContent.length()) {
                lines[i] = formattedContent.substring(i * columnWidth, Math.min((i + 1) * columnWidth, formattedContent.length()));
            } else {
                lines[i] = String.format("%-" + columnWidth + "s", ""); // הוספת רווחים למילוי
            }
        }
        return lines;
    }
}

//        //maybe change to row col
//         for(ArrayList<Cell> row : cellsMatrix)
//             for(Cell cell : row)
//                 cell.print();
