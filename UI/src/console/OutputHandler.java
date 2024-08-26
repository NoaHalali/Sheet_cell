package console;

import parts.SheetDTO;
import parts.cell.*;
import parts.cell.coordinate.Coordinate;
import parts.cell.expression.effectiveValue.CellType;
import parts.cell.expression.effectiveValue.EffectiveValue;

import java.util.List;

public class OutputHandler {

    public void printMenu(){
        System.out.println("Choose a number of action to preform:");
        for( ControllerImpl.MenuOption option: ControllerImpl.MenuOption.values() ){
            System.out.println(option.ordinal() + 1 + ". " + option);
        }
        System.out.println();
    }

    public void printSheetData(SheetDTO sheet)
    {
        System.out.println("Version: " + sheet.getVersion());
        System.out.println("Sheet Name: " + sheet.getName());
        System.out.println();
        printCellsMatrix(sheet);
        System.out.println();
    }

    public void printVersionsTable(List<Integer> versions) {
        // Define the column width based on the maximum length of the version numbers
        int columnWidth = Math.max(10, versions.stream()
                .map(String::valueOf)
                .mapToInt(String::length)
                .max()
                .orElse(0) + 2); // +2 for padding

        // Print the header row with version labels
        System.out.print("|");
        for (int i = 0; i < versions.size(); i++) {
            String versionLabel = "Version " + (i + 1);
            System.out.print(String.format("%-" + columnWidth + "s|", versionLabel));
        }
        System.out.println();

        // Print the data row with version numbers
        System.out.print("|");
        for (Integer version : versions) {
            System.out.print(String.format("%-" + columnWidth + "d|", version)); // Left-align numbers
        }
        System.out.println();
    }


    public void printCellsMatrix(SheetDTO sheet) {
        int numberOfCols = sheet.getNumberOfCols();
        int numberOfRows = sheet.getNumberOfRows();
        int columnWidth = sheet.getColumnWidth();
        int rowHeight = sheet.getRowHeight(); // גובה השורה שנקבע לכל הגיליון
        CellDTO[][] cellsMatrix = sheet.getCellsMatrix();

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

            // הדפסת כל תא לפי גובה השורה שהוגדר עבור הגיליון
            for (int line = 0; line < rowHeight; line++) {
                if (line > 0) {
                    System.out.print("   "); // רווח עבור מספרי השורות
                }
                for (int col = 0; col < numberOfCols; col++) {
                    CellDTO cell = cellsMatrix[row][col];
                    String cellEffectiveValue;

                    if (cell != null) {
                        cellEffectiveValue = calcValueToPrint(cell.getEffectiveValue());
                    } else {
                        cellEffectiveValue = "";
                    }

                    //TODO - in the next missions - add the option of overflow to next line (if possible according to the height of cell)
                    int startIndex = line * columnWidth;
                    System.out.print("|");
                    int strIndex = startIndex;
                    while (strIndex < startIndex + columnWidth && strIndex < cellEffectiveValue.length()) {
                        System.out.print(cellEffectiveValue.charAt(strIndex));
                        strIndex++;
                    }

                    while (strIndex < startIndex + columnWidth) {
                        System.out.print(" ");
                        strIndex++;
                    }
                }
                System.out.println(); // מעבר לשורה הבאה
            }
        }
    }


    public String calcValueToPrint(EffectiveValue effectiveValue) {
        //TODO- maybe add exceptions, try and catch
        if(effectiveValue.getCellType() == CellType.NUMERIC) {
            double num = effectiveValue.extractValueWithExpectation(Double.class);

            if (Double.isNaN(num) || Double.isInfinite(num)) {
                return "NaN"; // החזר "NaN" אם הערך הוא NaN או Infinity
            }

            StringBuilder str = new StringBuilder();

            // המרת double ל-int והשוואה
            int intValue = (int) num;
            if (num == intValue) {
                // במקרה שה-double שווה לערך int, נמשיך את העיבוד
                while (num > 1000) {
                    num = num / 1000;
                    double mod = num % 1000;
                    str.insert(0, "," + (int)mod);  // כאן נשמור רק את החלק השלם של התוצאה
                }
                double mod = num % 1000;
                str.insert(0, (int)mod);  // המרה ל-int כדי לשמור את החלק השלם בלבד
            } else {
                // במקרה שה-double אינו שווה לערך int, נעבד את המספר כ-double
                while (num > 1000) {
                    num = num / 1000;
                    double mod = num % 1000;
                    str.insert(0, "," + String.format("%.2f", mod));  // שמירה של המספר עם שלוש ספרות אחרי הנקודה
                }
                double mod = num % 1000;
                str.insert(0, String.format("%.2f", mod));  // שמירה של המספר עם שלוש ספרות אחרי הנקודה
            }

            return str.toString();  // מחזירים את התוצאה כמחרוזת
        }

        else if(effectiveValue.getCellType() == CellType.STRING)
        {
            return effectiveValue.extractValueWithExpectation(String.class).trim();
        }
        else if (effectiveValue.getCellType() == CellType.BOOLEAN)
        {
            return String.valueOf(effectiveValue.extractValueWithExpectation(Boolean.class)).toUpperCase();
        }
        else
        {
            throw new IllegalArgumentException(); //TODO - temporary
        }
    }

    public void printCellState(CellDTO cell, Coordinate coordinate){
        if(cell == null)
        {
            System.out.println("Cell at coordinate: " + coordinate + " is Empty.");
        }
        else {
            Coordinate coord = cell.getCoord();
            System.out.println("Cell identity: " + coord.toString());

            String originalValue = cell.getOriginalValue();
            System.out.println("Original Value: " + originalValue);

            EffectiveValue effectiveValue = cell.getEffectiveValue();
            System.out.println("Effective Value: " + calcValueToPrint(effectiveValue));

            int version = cell.getLastUpdatedVersion();
            System.out.println("Last Updated Version: " + version);

            List<String> dependsOnNames = cell.getDependsOn().stream().map(Coordinate::toString).toList();
            System.out.println("DependsOn Names: " + dependsOnNames);

            List<String> influencingOn = cell.getInfluencingOn().stream().map(Coordinate::toString).toList();
            System.out.println("InfluencingOn Names: " + influencingOn);

            System.out.println();
        }
    }

    public void printCellStateBeforeUpdate(CellDTO cell, Coordinate coordinate)
    {
        System.out.println("Cell identity: " + coordinate.toString());

        String originalValue = cell.getOriginalValue();
        System.out.println("Original Value: " + originalValue);

        EffectiveValue effectiveValue = cell.getEffectiveValue();
        System.out.println("Effective Value: " + calcValueToPrint(effectiveValue));
    }
}
