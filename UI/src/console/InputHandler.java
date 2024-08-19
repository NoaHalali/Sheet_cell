package console;

import console.ControllerImpl.MenuOption;
import parts.cell.Coordinate;
import parts.cell.CoordinateImpl;

import java.util.Scanner;

public class InputHandler {

    public Coordinate getCoordinateFromUser() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        boolean validInput = false;


        while (!validInput) {

          if(isValidCoordinateFormatInput(input)) {
                //TODO - specify
                System.out.println("Input must be in length 2 or 3 and start with capital letter and finish with 2 digit number for example : A23");

          }else{

//                int column = input.charAt(0) - 'A';
//                if(column < 0 || column > 20) {
//
//                }
//                int row = input.charAt(1) - '1';
//                if
            //בדיקה שהשורה והעמודה אכן בטווח TODO
                return new CoordinateImpl(row, column);
        }
     }
    }
    public boolean isValidCoordinateFormatInput(String input) {
        String pattern = "^[A-Z]+[0-9]+$";
        return input.matches(pattern);
    }

    public MenuOption getMenuOptionFromUser() {
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;
        boolean validInput = false;
        MenuOption selectedOption = null;

        while (!validInput) {
            try {

                userInput = Integer.parseInt(scanner.nextLine());


                if (userInput < 1 || userInput > 5) {
                    throw new IllegalArgumentException("The number must be between 1 and "+ MenuOption.values().length + ".");
                }
                selectedOption = MenuOption.values()[userInput - 1];

                validInput = true; // If we reach here, the input is valid.
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer between 1 and "+ MenuOption.values().length + ".");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        return selectedOption;
    }

}
