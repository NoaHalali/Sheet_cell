package console;

import parts.EngineImpl;
import parts.SheetDTO;
import parts.CellDTO;
import parts.cell.coordinate.Coordinate;

import java.util.List;

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
            }
            finally {
                System.out.println();
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
                    System.out.println("File loaded succesfully!");
                }
                catch (Exception e) {
                    System.out.println("File loading failed");
                    System.out.println(e.getMessage());
                    //System.out.println();
                }
//                finally {
//                    System.out.println();
//                }
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
                    controller.outputHandler.printSheetData(sheet);
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
                    Coordinate coordinate = controller.inputHandler.getCoordinateFromUser();
                    CellDTO cell = controller.engine.getCellDTOByCoordinate(coordinate);
                    if(cell != null) {
                        controller.outputHandler.printCellState(cell, coordinate);
                    }else{
                     int lastVersion=controller.engine.getLastVersionOfEmptyCell(coordinate) ;
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
                System.out.println("Please enter cell coordinate ");
                Coordinate coordinate = controller.inputHandler.getCoordinateFromUser();

                    CellDTO cell = controller.engine.getCellDTOByCoordinate(coordinate);

                    if (cell != null) {
                        controller.outputHandler.printCellStateBeforeUpdate(cell);
                    } else {
                        System.out.println("Trying create new cell");
                    }


                boolean gettingInputFromUser = true;
                String input;
                while (gettingInputFromUser) {

                    System.out.println("Please enter value you want: ");//לעשות חפירה שתסביר את הפורמט וקליטה בinput handler חלוקת אחריות על בידקות תקינות וכו,
                    input = controller.inputHandler.getInputFromUser();
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
                        input = controller.inputHandler.getInputFromUser();
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
                // printTable(controller.engine)
                try {
                    List<Integer> versions = controller.engine.getVersions();
                    controller.outputHandler.printVersionsTable(versions);
                    int version = controller.inputHandler.getVersionNumberFromUser();
                    SheetDTO sheet = controller.engine.getSheetDTOByVersion(version);
                    controller.outputHandler.printSheetData(sheet);
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
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