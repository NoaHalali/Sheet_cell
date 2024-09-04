package ActionLine;

import MainComponent.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import parts.CellDTO;
import parts.EngineImpl;
import parts.cell.coordinate.Coordinate;
import parts.cell.coordinate.CoordinateImpl;

public class ActionLineController {

    private AppController mainController;

    @FXML
    private Label cellId;
    @FXML
    private Label cellOriginalValue;
    @FXML
    private Button updateCell;
    @FXML
    private Label cellLastUpdateVersion;
    private EngineImpl engine;
    public void setActionLine(CellDTO cell) {
        if (cell != null) {
            cellId.setText(cell.getCoord().toString());
            cellOriginalValue.setText( cell.getOriginalValue());
            cellLastUpdateVersion.setText(String.valueOf(cell.getLastUpdatedVersion()));
            updateCell.setOnAction(event -> {
           System.out.println("Update button clicked!");
       });
       }else{
            cellId.setText("");
            cellOriginalValue.setText("");
            cellLastUpdateVersion.setText("");
        }
    }
//    @FXML
//    private void initialize() {
//        engine = new EngineImpl();
//        try {
//            engine.readFileData("C:\\Users\\amir\\IdeaProjects\\Sheet_cell\\engine\\src\\XMLFile\\GeneratedFiles\\dinamycTest.xml");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Coordinate coordinate = new CoordinateImpl(2, 2);
//        CellDTO cell = engine.getCellDTOByCoordinate(coordinate);
//
//        if (cell != null) {
//            cellId.setText(cell.getCoord().toString());
//            cellOriginalValue.setText(cell.getOriginalValue());
//            cellLastUpdateVersion.setText(String.valueOf(cell.getLastUpdatedVersion()));
//        }
//
//
//        // Add action handler for the update button if needed
//        updateCell.setOnAction(event -> {
//            // Handle the update action here
//            System.out.println("Update button clicked!");
//        });
//    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    }

