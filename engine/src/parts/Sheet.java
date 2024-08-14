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
        this.columnWidth = 3; // לדוגמה, אפשר להתאים את הערך על פי הדרישה
        this.rowHeight = 2; // לדוגמה, אפשר להתאים את הערך על פי הדרישה
    }

    public String getSheetName() {
        return name;
    }

    public int getVersionNumber() {
        return version;
    }

    //TODO - Move to UI or something
    public void printSheetCell()
    {
        System.out.println("Version: " + version);
        System.out.println("Sheet Name: " + name);
        System.out.println();
        printCellsMatrix();
    }


    public void printCellsMatrix() {
        // ריפוד לרוחב השורה עבור מספרי השורות
        for (int i = 0; i < 3; i++) {
            System.out.print(" ");
        }

        // הדפסת שמות העמודות
        for (int col = 0; col < numberOfCols; col++) {
            char columnName = (char) ('A' + col);
            System.out.print("|" + columnName);
            // הוספת רווחים בהתאם לרוחב העמודה
            for (int i = 1; i < columnWidth; i++) {
                System.out.print(" ");
            }
        }
        System.out.println();

        // הדפסת התאים בשורות ובעמודות
        for (int row = 0; row < numberOfRows; row++) {
            // הדפסת מספר שורה בפורמט של שתי ספרות
            String rowNumber = String.format("%02d", row + 1);
            System.out.print(rowNumber + " ");

            for (int col = 0; col < numberOfCols; col++) {
                Cell cell = cellsMatrix[row][col];
                String cellEffectiveValue = cell != null ? cell.geEffectiveValue() : ""; //צריך?

                System.out.print("|");
                int strIndex = 0;
                while (strIndex < cellEffectiveValue.length() && strIndex < columnWidth) {
                    System.out.print(cellEffectiveValue.charAt(strIndex));
                    strIndex++;
                }

                while(strIndex < columnWidth)
                {
                    System.out.print(" ");
                    strIndex++;
                }

                //TODO - in the next missions - add the option of overflow to next line (if possible according to the height of cell)

//                הדפסת ערך התא
//                System.out.print("|" + cellEffectiveValue);
//               הוספת רווחים אם התוכן קצר יותר מרוחב העמודה
//                for (int i = cellEffectiveValue.length(); i < columnWidth; i++) {
//                    System.out.print(" ");
//                }
            }
            System.out.println(); // מעבר לשורה הבאה
        }
    }

    public void setCellsMatrix(Cell[][] cellsMatrix) {
        this.cellsMatrix = cellsMatrix;
    }
}
