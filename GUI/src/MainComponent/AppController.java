package MainComponent;

import ActionLine.ActionLineController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import parts.CellDTO;
import parts.EngineImpl;
import parts.cell.coordinate.Coordinate;
import parts.cell.coordinate.CoordinateImpl;

public class AppController {
    private int count;
    private EngineImpl engine;

    @FXML
    private HBox actionLine;
    @FXML
    private ActionLineController actionLineController;

    @FXML
    private Button myButton=new Button();
    @FXML
    private Label myLabel ;

    @FXML
    private void initialize() {
        if(actionLineController!=null) {
            actionLineController.setMainController(this);
        }
        engine = new EngineImpl();
        try {
            engine.readFileData("C:\\Users\\amir\\IdeaProjects\\Sheet_cell\\engine\\src\\XMLFile\\GeneratedFiles\\dinamycTest.xml");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Coordinate coordinate =new CoordinateImpl(2,2);
        CellDTO cell=engine.getCellDTOByCoordinate(coordinate);

        //myLabel.setText(cell.getCoord().toString());
        actionLineController.setActionLine(cell);

    }
}




