package console;

import parts.cell.Coordinate;
import parts.cell.CoordinateImpl;
import console.ControllerImpl.MenuOption;
import java.util.Scanner;

public class InputHandler {


    public Coordinate getCoordinateFromUser() {

        Scanner scanner = new Scanner(System.in);
        String input;
        boolean validInput = false;
        Coordinate coordinate = null;

        while (!validInput) {
            input = scanner.nextLine();
            try {
                coordinate = parseCoordinate(input); // קריאה לפונקציית parseCoordinate המחזירה Coordinate ישירות
                validInput = true;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println("Input must start with a capital letter and end with a number, e.g., A23.");
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }

        return coordinate;
    }

    public Coordinate parseCoordinate(String input) {

        // בדיקה שהקלט תואם את הפורמט
        if (input.matches("^[A-Z]+[0-9]+$")) {
            // חילוץ האותיות (עמודה) והמספרים (שורה)
            String columnString = input.replaceAll("[0-9]", "");
            String rowString = input.replaceAll("[A-Z]", "");

            // המרת העמודה והשורה למספרים
            int column = columnStringToIndex(columnString);
            int row = Integer.parseInt(rowString);

            // החזרת אובייקט Coordinate
            return new CoordinateImpl(row, column);
        } else {
            throw new IllegalArgumentException("Invalid coordinate format. ");
        }
    }

    public int columnStringToIndex(String column) {
        int index = 0;
        for (int i = 0; i < column.length(); i++) {
            index = index * 26 + (column.charAt(i) - 'A' + 1);
        }
        return index ;
    }

    // הפונקציה הזו לא משתנה ונשארת כפי שכתבת
    public MenuOption getMenuOptionFromUser() {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;
        boolean validInput = false;
        MenuOption selectedOption = null;

        while (!validInput) {
            try {
                userInput = Integer.parseInt(scanner.nextLine());
                if (userInput < 1 || userInput > 5) {
                    throw new IllegalArgumentException("The number must be between 1 and " + MenuOption.values().length + ".");
                }
                selectedOption = MenuOption.values()[userInput - 1];
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer between 1 and " + MenuOption.values().length + ".");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        return selectedOption;
    }
    public String getFilePathFromUser(){
        System.out.println("Please enter a full path of the XML file you wish to load to the system: ");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        return path;
    }
}