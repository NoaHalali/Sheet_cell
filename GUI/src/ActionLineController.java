import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import parts.CellDTO;
import parts.EngineImpl;
import parts.cell.coordinate.Coordinate;
import parts.cell.coordinate.CoordinateImpl;

public class ActionLineController {
    @FXML
    private Label cell_id;
    @FXML
    private Label cell_original_value;
    @FXML
    private Button update_cell;
    @FXML
    private Label cell_last_update_version;
    private EngineImpl engine;
    @FXML
    private void initialize() {
        engine = new EngineImpl();
        try {
            engine.readFileData("C:\\Users\\amir\\IdeaProjects\\Sheet_cell\\engine\\src\\XMLFile\\GeneratedFiles\\dinamycTest.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Coordinate coordinate = new CoordinateImpl(2, 2);
        CellDTO cell = engine.getCellDTOByCoordinate(coordinate);

        if (cell != null) {
            cell_id.setText(cell.getCoord().toString());
            cell_original_value.setText(cell.getOriginalValue());
            cell_last_update_version.setText(String.valueOf(cell.getLastUpdatedVersion()));
        }

        // Add action handler for the update button if needed
        update_cell.setOnAction(event -> {
            // Handle the update action here
            System.out.println("Update button clicked!");
        });
    }

    }

