package console;

import parts.EngineImpl;
import parts.SheetDTO;
import parts.cell.CellDTO;
import parts.cell.coordinate.Coordinate;

import java.util.Scanner;

public class ControllerImpl implements Controller {

    private EngineImpl engine = new EngineImpl();
    private InputHandler inputHandler = new InputHandler();
    private OutputHandler outputHandler = new OutputHandler();
    private boolean systemIsRunning = true;

    @Override
    public void runSystem()
    {
        while (systemIsRunning) {
            outputHandler.printMenu();
            MenuOption option =inputHandler.getMenuOptionFromUser();
            try {
                option.execute(this);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
    public enum MenuOption {

        //1
        LOAD_FILE {
            @Override
            public void execute(ControllerImpl controller) {
                try {
                    String path = controller.inputHandler.getFilePathFromUser();
                    controller.engine.readFileData(path);
                    System.out.println("File loaded succesfully!\n");
                }
                catch (Exception e) {
                    System.out.println("File loading failed");
                    System.out.println(e.getMessage());
                    System.out.println();
                }
            }
            @Override
            public String toString() {
                return "Load a file to the system";
            }

        },
        //2
        DISPLAY_SHEET {
            @Override
            public void execute(ControllerImpl controller) {
                SheetDTO sheet = controller.engine.getCurrentSheetDTO();
                controller.outputHandler.printSheetData(sheet);
            }
            @Override
            public String toString() {
                return "Display current sheet state";
            }
        },
        //3
        DISPLAY_CELL {
            @Override
            public void execute(ControllerImpl controller) {
                try {
                    System.out.println("Please enter cell coordinate ");
                    Coordinate coordinate = controller.inputHandler.getCoordinateFromUser();
                    CellDTO cell = controller.engine.getCellDTOByCoordinate(coordinate);
                    controller.outputHandler.printCellState(cell);
                }

                catch (Exception e)
                {
                    //TODO -
                    // add checking if cell is out of bounds -V
                    // what if empty?
                    System.out.println(e.getMessage());
                }

            }
            @Override
            public String toString() {
                return "Display a cell state";
            }
        },
        //4
        UPDATE_CELL {
            @Override
            public void execute(ControllerImpl controller) {
                System.out.println("Please enter cell coordinate ");
                Coordinate coordinate = controller.inputHandler.getCoordinateFromUser();
                System.out.println("Please enter value you want ");//לעשות חפירה שתסביר את הפורמט וקליטה בinput handler חלוקת אחריות על בידקות תקינות וכו,
                Scanner scanner = new Scanner(System.in);//input handler
                String input = scanner.nextLine();//input handler
                controller.engine.updateCellValueFromOriginalValue(input, coordinate);
            }
            @Override
            public String toString() {
                return "Update cell value";
            }
        },
        //5
        DISPLAY_VERSIONS {
            @Override
            public void execute(ControllerImpl controller) {
                System.out.println("Deleting item...");
                // Add delete logic here
            }
            @Override
            public String toString() {
                return "Display system versions";
            }
        },
        //6
        EXIT {
            @Override
            public void execute(ControllerImpl controller) {
                controller.systemIsRunning=false;
            }
            @Override
            public String toString() {
                return "Exit";
            }
        };

        // Abstract method to be implemented by each enum constant
        public abstract void execute(ControllerImpl controller);
    }

}
