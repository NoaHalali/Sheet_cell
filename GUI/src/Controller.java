import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import parts.CellDTO;
import parts.EngineImpl;
import parts.cell.coordinate.Coordinate;
import parts.cell.coordinate.CoordinateImpl;

public class Controller {
    private int count;
    private EngineImpl engine;
    @FXML
    private Button myButton=new Button();

    @FXML
    private Label myLabel ;


    @FXML
    private void initialize() {
        engine = new EngineImpl();
        try {
            engine.readFileData("C:\\Users\\amir\\IdeaProjects\\Sheet_cell\\engine\\src\\XMLFile\\GeneratedFiles\\dinamycTest.xml");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Coordinate coordinate =new CoordinateImpl(2,2);

        CellDTO cell=engine.getCellDTOByCoordinate(coordinate);
        myLabel.setText(cell.getCoord().toString());

    }
}




