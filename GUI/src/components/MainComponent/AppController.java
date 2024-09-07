package components.MainComponent;

import components.left.commands.CommandsController;
import components.left.ranges.RangesController;
import components.top.actionLine.ActionLineController;
import components.center.cellsTable.TableController;
import components.top.fileChooser.FileChooserController;
import components.top.versions.VersionSelectorController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import parts.cell.CellDTO;
import parts.EngineImpl;
import parts.SheetDTO;
import parts.cell.coordinate.Coordinate;
import parts.cell.coordinate.CoordinateImpl;

import java.io.File;
import java.util.Arrays;

public class AppController {

    //Properties
    private SimpleBooleanProperty isFileSelected;

    //Components
    @FXML private GridPane actionLine;
    @FXML private HBox fileChooser;
    @FXML private ScrollPane table;
    @FXML private Button myButton=new Button();
    @FXML private Label myLabel ;
    @FXML private VBox commands;
    @FXML private VBox ranges;
    @FXML private HBox versionSelector;


    //Controllers
    @FXML private ActionLineController actionLineController;
    @FXML private TableController tableController;
    @FXML private FileChooserController fileChooserController;
    @FXML private CommandsController commandsController;
    @FXML private RangesController rangesController;
    @FXML private VersionSelectorController versionSelectorController; // רפרנס לקונטרולר של הרכיב הקטן


    private Stage primaryStage;
    private int count;
    private Coordinate coordinate;
    private EngineImpl engine;

    @FXML
    private void initialize() {
        if (actionLineController != null && tableController != null && fileChooserController != null && versionSelectorController != null) {
            actionLineController.setMainController(this);
            tableController.setMainController(this);
            versionSelectorController.setMainController(this);
            fileChooserController.setMainController(this);

            isFileSelected = new SimpleBooleanProperty(false);
            table.disableProperty().bind(isFileSelected.not());
            actionLine.disableProperty().bind(isFileSelected.not());
            versionSelector.disableProperty().bind(isFileSelected.not());

            engine = new EngineImpl();

            // טוען את רכיב ה-Version ומקשר את הקונטרולר שלו
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/top/versions/versionSelector.fxml"));
//                Parent versionRoot = loader.load();
//                versionSelectorController = loader.getController(); // חילוץ הקונטרולר של הרכיב הקטן
//                versionSelector.getChildren().add(versionRoot); // הוספת הרכיב למיכל המתאים
//                versionSelectorController.initializeDefaultState();
//
////                // טוען את רשימת הגרסאות
////                setVersionSelectorOptions();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//            // הגדרת ה-ComboBox מרכיב הגרסאות
//            ComboBox<String> versionSelector = versionSelectorController.getVersionSelector();
//            versionSelector.getItems().addAll("Version 1");
//
//            // הוספת מאזין לערך הנבחר
//            versionSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//                if (newValue != null) {
//                    handleVersionSelection(newValue); // קריאה למתודה שמטפלת באירוע
//                }
//            });

        }
    }

    private void setVersionSelectorOptions() {
        // טוען את ה-DTO ומעדכן את הגרסאות בקונטרולר של הרכיב הקטן
//        VersionDTO versionDTO = new VersionDTO();
//        List<String> versions = versionDTO.getVersions();

        int numOfVersions = engine.getNumOfVersions();
        versionSelectorController.setVersionSelectorOptions(numOfVersions);
        //versionSelectorController.setVersions(versions); // קריאה למתודה שמעדכנת את הגרסאות ברכיב הקטן
    }

//    private void handleVersionSelection(String version) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Version Information");
//        alert.setHeaderText("Selected Version: " + version);
//        alert.setContentText("Details about " + version + " will appear here.");
//        alert.showAndWait();
//
//    }
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
    public void updateCellValue(String value) throws Exception {
        engine.updateCellValue(value, coordinate);
        SheetDTO sheet = engine.getCurrentSheetDTO();
        CellDTO[][] cells = sheet.getCellsMatrix();
        Arrays.stream(cells).flatMap(Arrays::stream).forEach(cell -> {
            if(cell!=null) {
                tableController.updateCellContent(cell.getCoord(), cell.getEffectiveValue());
            }

        });
    }
    //public void

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
                versionSelectorController.initializeVersionSelector();

                setVersionSelectorOptions();
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