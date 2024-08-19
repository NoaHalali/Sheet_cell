package console;

public class Menu {

    public enum MenuOption {

        //sub question
        //sub "menasha" ,3.4 ,4 is okay ?/????/
        LOAD_FILE {
            @Override
            public void execute() {
                System.out.println("Viewing items...");
                // Add view logic here
            }
            @Override
            public String toString() {
                return "View Items";
            }

        },
        PRINT_SHEET {
            @Override
            public void execute() {

            }
            @Override
            public String toString() {
                return "View Items";
            }
        },
        PRINT_CELL {
            @Override
            public void execute() {

                System.out.println("Please, enter cell coordinate ");
                //קלט
                // בדיקת תקינות והבאת CELLDTO
                //outputHadlern קריאה למתודה

            }
            @Override
            public String toString() {
                return "View Items";
            }
        },
        UPDATE_CELL {
            @Override
            public void execute() {
                System.out.println("Deleting item...");
                // Add delete logic here
            }
            @Override
            public String toString() {
                return "View Items";
            }
        },
        PRINT_VERSIONS {
            @Override
            public void execute() {
                System.out.println("Deleting item...");
                // Add delete logic here
            }
            @Override
            public String toString() {
                return "View Items";
            }
        },
        EXIT {
            @Override
            public void execute() {
                System.out.println("Exiting...");
            }
            @Override
            public String toString() {
                return "View Items";
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
