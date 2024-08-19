package console;

public class Menu {

    public enum MenuOption {
        LOAD_FILE {
            @Override
            public void execute() {
                System.out.println("Viewing items...");
                // Add view logic here
            }
        },
        PRINT_SHEET {
            @Override
            public void execute() {
                System.out.println("Adding item...");
                // Add add logic here
            }
        },
        PRINT_CELL {
            @Override
            public void execute() {
                System.out.println("Editing item...");
                // Add edit logic here
            }
        },
        UPDATE_CELL {
            @Override
            public void execute() {
                System.out.println("Deleting item...");
                // Add delete logic here
            }
        },
        PRINT_VERSIONS {
            @Override
            public void execute() {
                System.out.println("Deleting item...");
                // Add delete logic here
            }
        },
        EXIT {
            @Override
            public void execute() {
                System.out.println("Exiting...");
            }
        };

        // Abstract method to be implemented by each enum constant
        public abstract void execute();
    }

    public void printMenu() {
        System.out.println("Choose a number of action to perform:");
        ///
        ///
    }
}
