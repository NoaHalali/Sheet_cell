package console;

import parts.EngineImpl;
import parts.cell.CellDTO;
import parts.cell.Coordinate;

public class ControllerImpl implements Controller {

    private EngineImpl engine = new EngineImpl();
    private InputHandler inputHandler = new InputHandler();
    private OutputHandler outputHandler = new OutputHandler();
    private Menu menu = new Menu();
    private boolean systemIsRunning = true;

    @Override
    public void runSystem()
    {
        while (systemIsRunning) {
            outputHandler.printMenu();
            MenuOption option =inputHandler.getMenuOptionFromUser();
            option.execute(this);
        }
    }
    public enum MenuOption {

        //sub question
        //sub "menasha" ,3.4 ,4 is okay ?/????/
        LOAD_FILE {
            @Override
            public void execute(ControllerImpl controller) {
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
            public void execute(ControllerImpl controller) {

            }
            @Override
            public String toString() {
                return "View Items";
            }
        },
        PRINT_CELL {
            @Override
            public void execute(ControllerImpl controller) {


                System.out.println("Please, enter cell coordinate ");
                Coordinate coordinate = cont;
                CellDTO cell = controller.engine.getCellDTOByCoordinate(coordinate);
                controller.outputHandler.showCellState(cell);
                //outputHadlern קריאה למתודה

            }
            @Override
            public String toString() {
                return "View Items";
            }
        },
        UPDATE_CELL {
            @Override
            public void execute(ControllerImpl controller) {
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
            public void execute(ControllerImpl controller) {
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
            public void execute(ControllerImpl controller) {
                controller.systemIsRunning=false;
            }
            @Override
            public String toString() {
                return "View Items";
            }
        };

        // Abstract method to be implemented by each enum constant
        public abstract void execute(ControllerImpl controller);
    }
    public void displaySheetToUser(){
       //Todo SheetDTO tmpSheet=engine.;
    }
//
//    public void displayCellData(String cellId) {
//        //TODO - validation checking
//
//        CellDTO cellData = engine.getCellData(cellId);
//        System.out.println("Cell ID: " + cellId);
//        System.out.println("Original Value: " + cellData.getOriginalValue());
//        System.out.println("Effective Value: " + cellData.getEffectiveValue());
//        System.out.println("Version: " + cellData.getVersionNumber());
//        System.out.println("Dependencies: " + String.join(", ", cellData.getDependencies()));
//    }





}
