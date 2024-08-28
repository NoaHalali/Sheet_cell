package console;

import parts.CellDTO;
import parts.SheetDTO;
import parts.cell.coordinate.Coordinate;
import parts.cell.coordinate.CoordinateImpl;
import parts.cell.expression.effectiveValue.CellType;
import parts.cell.expression.effectiveValue.EffectiveValue;

import java.util.List;
import java.util.Scanner;

public class InputOutputHandler {

    public void printMenu(){
        System.out.println("-------------------------------------");
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
        System.out.println("\n");

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
        if(effectiveValue.getCellType() == CellType.NUMERIC) {
            double num = effectiveValue.extractValueWithExpectation(Double.class);

            if (Double.isNaN(num) || Double.isInfinite(num)) {
                return "NaN"; // החזר "NaN" אם הערך הוא NaN או Infinity
            }

            StringBuilder str = new StringBuilder();
            if (num == (int) num) {
                return String.format("%,d", (int) num); // Format as integer with commas
            } else {
                return String.format("%,.2f", num); // Format as floating point with commas and two decimal places
            }

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
            System.out.println("Original value: " + originalValue);

            EffectiveValue effectiveValue = cell.getEffectiveValue();
            System.out.println("Effective value: " + calcValueToPrint(effectiveValue));

            int version = cell.getLastUpdatedVersion();
            System.out.println("Last updated version: " + version);

            List<String> dependsOnNames = cell.getDependsOn().stream().map(Coordinate::toString).toList();
            System.out.println("Depends on the cells: " + dependsOnNames);

            List<String> influencingOn = cell.getInfluencingOn().stream().map(Coordinate::toString).toList();
            System.out.println("Influencing on the cells: " + influencingOn);
        }
    }

    public void printCellStateBeforeUpdate(CellDTO cell)
    {
        System.out.println("Cell identity: " + cell.getCoord());

        String originalValue = cell.getOriginalValue();
        System.out.println("Original value: " + originalValue);

        EffectiveValue effectiveValue = cell.getEffectiveValue();
        System.out.println("Effective value: " + calcValueToPrint(effectiveValue));
    }

    public Coordinate getCoordinateFromUser() throws IllegalArgumentException {

        Scanner scanner = new Scanner(System.in);
        String input;
        //TODO - if there is time, replace to the loop below with option of exit to menu
        input = scanner.nextLine();
        return CoordinateImpl.parseCoordinate(input);

        //This option is with a loop
//       Scanner scanner = new Scanner(System.in);
//       String input;
//       boolean validInput = false;
//        while (!validInput) {
//            input = scanner.nextLine();
//            try {
//                coordinate = parseCoordinate(input);
//                validInput = true;
//            } catch (IllegalArgumentException e) {
//                System.out.println(e.getMessage());
//                System.out.println("Input must start with a capital letter and end with a number, e.g., A23.");
//            } catch (Exception e) {
//                System.out.println("Unexpected error: " + e.getMessage());
//            }
//        }

    }

    public ControllerImpl.MenuOption getMenuOptionFromUser() {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;
        boolean validInput = false;
        ControllerImpl.MenuOption selectedOption = null;

        while (!validInput) {
            try {
                userInput = Integer.parseInt(scanner.nextLine());
                if (userInput < 1 || userInput > 8) {
                    System.out.println("Invalid option number!\n" +
                            "Option number must be between 1 and " + ControllerImpl.MenuOption.values().length + ".\n" +
                            "Try again:" );
                }
                else
                {
                    selectedOption = ControllerImpl.MenuOption.values()[userInput - 1];
                    validInput = true;
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid integer between 1 and " + ControllerImpl.MenuOption.values().length + ":");
            }
        }

        return selectedOption;
    }

    public int getVersionNumberFromUser()
    {
        System.out.println("Enter the version number:");
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;

        try {
            userInput = Integer.parseInt(scanner.nextLine());
        }
        catch (NumberFormatException e){
            throw new NumberFormatException("version must be Integer ! \ngoing to Main menu");
        }
        return userInput;
    }

    public String getFilePathFromUser(){
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        return path;
    }

    public String getInputFromUser(){
        Scanner scanner = new Scanner(System.in);
        String originalValue = scanner.nextLine();
        return originalValue;
    }

}
