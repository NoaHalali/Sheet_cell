package console;

import console.ControllerImpl.MenuOption;

import java.util.Scanner;

public class InputHandler {

    public MenuOption getMenuOption() {
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
