package MainComponent;

import ActionLine.ActionLineController;
import CellsTable.TableController;
import FileChooser.FileChooserController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import parts.CellDTO;
import parts.EngineImpl;
import parts.SheetDTO;
import parts.cell.coordinate.Coordinate;
import parts.cell.coordinate.CoordinateImpl;

import java.io.File;

public class AppController {
    private int count;
    private Coordinate coordinate;
    private EngineImpl engine;
    private SimpleBooleanProperty isFileSelected;
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
    private Stage primaryStage;


    @FXML
    private void initialize() {
        if(actionLineController!=null && tableController!=null && fileChooserController!=null) {
            actionLineController.setMainController(this);
            tableController.setMainController(this);
            fileChooserController.setMainController(this);
            isFileSelected = new SimpleBooleanProperty(false);
            table.disableProperty().bind(isFileSelected.not());
            actionLine.disableProperty().bind(isFileSelected.not());

        }





        engine = new EngineImpl();
//        String amirfile = "C:\\Users\\amir\\IdeaProjects\\Sheet_cell\\engine\\src\\XMLFile\\GeneratedFiles\\dinamycTest.xml";
//        String noafile = "C:\\Users\\noa40\\OneDrive - The Academic College of Tel-Aviv Jaffa - MTA\\שנה ב\\קורסי בחירה\\פיתוח תוכנה מבוסס גאווה\\מטלות\\שטיסל\\shticell\\engine\\src\\XMLFile\\GeneratedFiles\\dinamycTest.xml";
//
//        try {
//            engine.readFileData(amirfile);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        tableController.initializeGrid(engine.getCurrentSheetDTO());
//        Coordinate coordinate =new CoordinateImpl(2,2);
//        CellDTO cell=engine.getCellDTOByCoordinate(coordinate);
//
//        //myLabel.setText(cell.getCoord().toString());
//        actionLineController.setActionLine(cell);

    }
    public void updateActionLine(String coord) {
        if(coordinate != null){
            tableController.RemoveFocusingOfCell(coordinate.toString());
        }

            coordinate = CoordinateImpl.parseCoordinate(coord);
            CellDTO cell = engine.getCellDTOByCoordinate(coordinate);
            actionLineController.setActionLine(cell);

    }
    public void SetFileSelected() {
        isFileSelected.set(true);
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public File showFileSelector(FileChooser fileChooser) {
        return fileChooser.showOpenDialog(primaryStage);
    }

    public void loadFile(String absolutePath) throws Exception {
        // יצירת משימה לטעינת הקובץ ברקע
        Task<Void> loadFileTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // קריאת נתוני הקובץ
                engine.readFileData(absolutePath);
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                // עדכון ה-UI במקרה של הצלחה
                isFileSelected.set(true);
                SheetDTO sheet = engine.getCurrentSheetDTO();
                tableController.initializeGrid(sheet);
            }

            @Override
            protected void failed() {
                super.failed();
                // טיפול בשגיאה במקרה של כשל בטעינת הקובץ
                System.out.println("Failed to load file: " + getException().getMessage());
            }
        };

        // הפעלת המשימה ב-Thread נפרד
        new Thread(loadFileTask).start();
    }
}