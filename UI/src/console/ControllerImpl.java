package console;

import parts.Engine;
import parts.EngineImpl;
import parts.SheetDTO;
import parts.cell.CellDTO;
import parts.cell.coordinate.Coordinate;

import java.util.List;

public class ControllerImpl implements Controller {

    private Engine engine = new EngineImpl();
    private InputOutputHandler inputOutputHandler = new InputOutputHandler();
    private boolean systemIsRunning = true;

    @Override
    public void runSystem()
    {
        System.out.println("Welcome to the Shticell!");
        while (systemIsRunning) {
            inputOutputHandler.printMenu();
            MenuOption option =inputOutputHandler.getMenuOptionFromUser();
            try {
                option.execute(this);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
            finally {
                System.out.println();
            }

        }
        System.out.println("Bye Bye!");

    }

    public enum MenuOption {

        //1
        LOAD_FILE {
            @Override
            public void execute(ControllerImpl controller) {
                try {
                    System.out.println("Please enter a full path of the XML file you wish to load to the system: ");
                    String path = controller.inputOutputHandler.getFilePathFromUser();
                    controller.engine.readFileData(path);
                    System.out.println("File loaded successfully!");
                }
                catch (Exception e) {
                    System.out.println("File loading failed");
                    System.out.println(e.getMessage());
                }
//
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
                try{
                    SheetDTO sheet = controller.engine.getCurrentSheetDTO();
                    controller.inputOutputHandler.printSheetData(sheet);
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }

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
                    if (!controller.engine.sheetLoadad())
                    {
                        System.out.println("Sheet is not loaded. Please load a sheet before attempting to access it.");
                        return;
                    }
                    System.out.println("Please enter cell coordinate ");
                    Coordinate coordinate = controller.inputOutputHandler.getCoordinateFromUser();
                    CellDTO cell = controller.engine.getCellDTOByCoordinate(coordinate);
                    if(cell != null) {
                        controller.inputOutputHandler.printCellState(cell, coordinate);
                    }else{
                    // int lastVersion=controller.engine.getLastVersionOfEmptyCell(coordinate) ;
                     int lastVersion= 0; //temporary because doesn't work with the java fx version of code
                     System.out.println("Cell at coordinate: " + coordinate + " is empty");
                     if(lastVersion==0)
                     {
                         System.out.println("Cell was never updated");
                     }
                     else{
                         System.out.println("Cell was deleted in version "+lastVersion);
                     }
                    }

                }

                catch (Exception e)
                {
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
                if (!controller.engine.sheetLoadad())
                {
                    System.out.println("Sheet is not loaded. Please load a sheet before attempting to access it.");
                    return;
                }
                System.out.println("Please enter cell coordinate ");
                Coordinate coordinate = controller.inputOutputHandler.getCoordinateFromUser();

                    CellDTO cell = controller.engine.getCellDTOByCoordinate(coordinate);

                    if (cell != null) {
                        controller.inputOutputHandler.printCellStateBeforeUpdate(cell);
                    } else {
                        System.out.println("Trying create new cell");
                    }


                boolean gettingInputFromUser = true;
                String input;
                while (gettingInputFromUser) {

                    System.out.println("Please enter value you want: ");//לעשות חפירה שתסביר את הפורמט וקליטה בinput handler חלוקת אחריות על בידקות תקינות וכו,
                    input = controller.inputOutputHandler.getInputFromUser();
                    try {
                        controller.engine.updateCellValue(input, coordinate);
                        System.out.println("Cell updated successfully!");
                        DISPLAY_SHEET.execute(controller);
                        gettingInputFromUser = false;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println();
                        System.out.println("Do you want to try again? " +
                                "Press 1 to try again, or any other key to return to the main menu.");
                        input = controller.inputOutputHandler.getInputFromUser();
                        if (!input.trim().equals("1")) {
                            gettingInputFromUser = false;
                        }
                    }
                }
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
                boolean gettingInputFromUser = true;
                String input;
                while(gettingInputFromUser)
                {
                    try {
                        List<Integer> versions = controller.engine.getVersions();
                        controller.inputOutputHandler.printVersionsTable(versions);
                        int version = controller.inputOutputHandler.getVersionNumberFromUser();
                        SheetDTO sheet = controller.engine.getSheetDTOByVersion(version);
                        controller.inputOutputHandler.printSheetData(sheet);
                        gettingInputFromUser = false;
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println();
                        System.out.println("Do you want to try again? " +
                                "Press 1 to try again, or any other key to return to the main menu.");
                        input = controller.inputOutputHandler.getInputFromUser();
                        if (!input.trim().equals("1")) {
                            gettingInputFromUser = false;
                        }
                    }
                }

            }
            @Override
            public String toString() {
                return "Display system versions";
            }
        },

        //6
        SAVE_SYSTEM_STATE {
            @Override
            public void execute(ControllerImpl controller) {
                try {
                    if (!controller.engine.sheetLoadad())
                    {
                        System.out.println("Sheet is not loaded. Please load a sheet before attempting to access it.");
                        return;
                    }
                    System.out.println("Please enter a full path of the file you wish to save the system data to: ");
                    String path = controller.inputOutputHandler.getFilePathFromUser();
                    controller.engine.saveSystemState(path);
                    System.out.println("System state saved successfully!");
                }
                catch (Exception e) {
                    System.out.println("System state saving failed");
                    System.out.println(e.getMessage());
                }
            }
            @Override
            public String toString() {
                return "Save system state to a file";
            }
        },
        //7
        LOAD_SYSTEM_STATE {
            @Override
            public void execute(ControllerImpl controller) {
                try {
                    System.out.println("Please enter a full path of the file you wish to load the system state from: ");
                    String path = controller.inputOutputHandler.getFilePathFromUser();
                    controller.engine.loadSystemState(path);
                    System.out.println("System state loaded successfully!");
                }
                catch (Exception e) {
                    System.out.println("System state loading failed");
                    System.out.println(e.getMessage());
                }
            }
            @Override
            public String toString() {
                return "Load system state from a file";
            }
        },
        //8
        EXIT {
            @Override
            public void execute(ControllerImpl controller) {
                controller.systemIsRunning = false;
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