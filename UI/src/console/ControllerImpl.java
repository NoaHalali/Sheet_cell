package console;

import parts.EngineImpl;
public class ControllerImpl implements Controller {

    private EngineImpl engine = new EngineImpl();
    private InputHandler inputHandler = new InputHandler();
    private OutputHandler outputHandler = new OutputHandler();
    boolean systemIsRunning = true;

    @Override
    public void runSystem()
    {
        while (systemIsRunning) {
            outputHandler.printMenu();
            //
            //


        }
    }

    public void displayCellData(String cellId) {
        CellDTO cellData = engine.getCellData(cellId);
        System.out.println("Cell ID: " + cellId);
        System.out.println("Original Value: " + cellData.getOriginalValue());
        System.out.println("Effective Value: " + cellData.getEffectiveValue());
        System.out.println("Version: " + cellData.getVersionNumber());
        System.out.println("Dependencies: " + String.join(", ", cellData.getDependencies()));
    }





}
