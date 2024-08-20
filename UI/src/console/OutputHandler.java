package console;

import parts.cell.CellDTO;
import parts.cell.Coordinate;
import parts.cell.EffectiveValue;

import java.util.List;

public class OutputHandler {

    public void printMenu(){
        System.out.println("Choose a number of action to preform:");
        for( ControllerImpl.MenuOption option: ControllerImpl.MenuOption.values() ){
            System.out.println(option.ordinal() + 1 + ". " + option);
        }
    }

//    public void printSheetData(SheetDTO sheet)
//    {
//
//        System.out.println("Version: " + version);
//        System.out.println("Sheet Name: " + name);
//        System.out.println();
//        printCellsMatrix();
//    }
//
//    public void printCellsMatrix() {
//        // ריפוד לרוחב השורה עבור מספרי השורות
//        for (int i = 0; i < 3; i++) {
//            System.out.print(" ");
//        }
//
//        // הדפסת שמות העמודות
//        for (int col = 0; col < numberOfCols; col++) {
//            char columnName = (char) ('A' + col);
//            System.out.print("|" + columnName);
//            // הוספת רווחים בהתאם לרוחב העמודה
//            for (int i = 1; i < columnWidth; i++) {
//                System.out.print(" ");
//            }
//        }
//        System.out.println();
//
//        // הדפסת התאים בשורות ובעמודות
//        for (int row = 0; row < numberOfRows; row++) {
//            // הדפסת מספר שורה בפורמט של שתי ספרות
//            String rowNumber = String.format("%02d", row + 1);
//            System.out.print(rowNumber + " ");
//
//            for (int col = 0; col < numberOfCols; col++) {
//                Cell cell = cellsMatrix[row][col];
//                String cellEffectiveValue = cell != null ? String.valueOf(cell.geEffectiveValue().getValue()) : ""; //צריך?
//
//                System.out.print("|");
//                int strIndex = 0;
//                while (strIndex < cellEffectiveValue.length() && strIndex < columnWidth) {
//                    System.out.print(cellEffectiveValue.charAt(strIndex));
//                    strIndex++;
//                }
//
//                while(strIndex < columnWidth)
//                {
//                    System.out.print(" ");
//                    strIndex++;
//                }
//
//                //TODO - in the next missions - add the option of overflow to next line (if possible according to the height of cell)
//
////                הדפסת ערך התא
////                System.out.print("|" + cellEffectiveValue);
////               הוספת רווחים אם התוכן קצר יותר מרוחב העמודה
////                for (int i = cellEffectiveValue.length(); i < columnWidth; i++) {
////                    System.out.print(" ");
////                }
//            }
//            System.out.println(); // מעבר לשורה הבאה
//        }
//    }

    public void showCellState(CellDTO cell){

        Coordinate coord = cell.getCoord();
        System.out.println("Cell identity: " + coord.toString());


        String originalValue = cell.getOriginalValue();
        System.out.println("Original Value: " + originalValue);

        //TODO - implement the toString()
        EffectiveValue effectiveValue = cell.getEffectiveValue();
        System.out.println("Effective Value: " + effectiveValue);

        int version = cell.getLastUpdatedVersion();
        System.out.println("Last Updated Version: " + version);

        List<String> dependsOnNames = cell.getDependsOn().stream().map(Coordinate::toString).toList();
        System.out.println("DependsOn Names: " + dependsOnNames);

        List<String> influencingOn = cell.getInfluencingOn().stream().map(Coordinate::toString).toList();
        System.out.println("InfluencingOn Names: " + influencingOn);
    }
}
