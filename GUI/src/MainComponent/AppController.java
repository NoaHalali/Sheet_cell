package MainComponent;

import ActionLine.ActionLineController;
import CellsTable.TableController;
import FileChooser.FileChooserController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import parts.CellDTO;
import parts.EngineImpl;
import parts.cell.coordinate.Coordinate;
import parts.cell.coordinate.CoordinateImpl;

public class AppController {
    private int count;
    private Coordinate coordinate;
    private EngineImpl engine;

    @FXML
    private HBox actionLine;
    @FXML
    private ActionLineController actionLineController;
    @FXML
    private HBox fileChooser;
    @FXML
    private FileChooserController fileChooserController;
    @FXML
    private ScrollPane table;
    @FXML
    private TableController tableController;

    @FXML
    private Button myButton=new Button();
    @FXML
    private Label myLabel ;



    @FXML
    private void initialize() {
        if(actionLineController!=null && tableController!=null && fileChooserController!=null) {
            actionLineController.setMainController(this);
            tableController.setMainController(this);
            fileChooserController.setMainController(this);
        }






        String amirfile = "C:\\Users\\amir\\IdeaProjects\\Sheet_cell\\engine\\src\\XMLFile\\GeneratedFiles\\dinamycTest.xml";
        String noafile = "C:\\Users\\noa40\\OneDrive - The Academic College of Tel-Aviv Jaffa - MTA\\שנה ב\\קורסי בחירה\\פיתוח תוכנה מבוסס גאווה\\מטלות\\שטיסל\\shticell\\engine\\src\\XMLFile\\GeneratedFiles\\dinamycTest.xml";
        engine = new EngineImpl();
        try {
            engine.readFileData(noafile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        tableController.initializeGrid(engine.getCurrentSheetDTO());
        Coordinate coordinate =new CoordinateImpl(2,2);
        CellDTO cell=engine.getCellDTOByCoordinate(coordinate);

        //myLabel.setText(cell.getCoord().toString());
        actionLineController.setActionLine(cell);

    }
    public void updateActionLine(String coord) {
        if(coordinate != null){
            tableController.RemoveFocusingOfCell(coordinate.toString());
        }
        if(actionLineController != null) {
            coordinate = CoordinateImpl.parseCoordinate(coord);
            CellDTO cell = engine.getCellDTOByCoordinate(coordinate);
            actionLineController.setActionLine(cell);
        }
    }
}




