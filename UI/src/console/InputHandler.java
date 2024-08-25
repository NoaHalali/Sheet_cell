package console;

import parts.cell.coordinate.Coordinate;
import parts.cell.coordinate.CoordinateImpl;
import console.ControllerImpl.MenuOption;
import java.util.Scanner;

public class InputHandler {


    public Coordinate getCoordinateFromUser() throws IllegalArgumentException {

        Scanner scanner = new Scanner(System.in);
        String input;
        //boolean validInput = false;
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





    public MenuOption getMenuOptionFromUser() {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;
        boolean validInput = false;
        MenuOption selectedOption = null;

        while (!validInput) {
            try {
                userInput = Integer.parseInt(scanner.nextLine());
                if (userInput < 1 || userInput > 6) {
                    System.out.println("Invalid option number!\n" +
                            "Option number must be between 1 and " + MenuOption.values().length + ".\n" +
                            "Try again:" );
//                    throw new IllegalArgumentException("Invalid option!\n" +
//                            "Option number must be between 1 and " + MenuOption.values().length + ".");
                }
                else
                {
                    selectedOption = MenuOption.values()[userInput - 1];
                    validInput = true;
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid integer between 1 and " + MenuOption.values().length + ":");
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

    public String getInputFromUser(){
        Scanner scanner = new Scanner(System.in);
        String originalValue = scanner.nextLine();
        return originalValue;
    }


}