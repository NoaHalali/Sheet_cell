package components.MainComponent;

import components.left.commands.CommandsController;
import components.left.ranges.RangesController;
import components.top.actionLine.ActionLineController;
import components.center.cellsTable.TableController;
import components.top.fileChooser.FileChooserController;
import components.top.versions.VersionSelectorController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import parts.Engine;
import parts.cell.CellDTO;
import parts.EngineImpl;
import parts.SheetDTO;
import parts.cell.coordinate.Coordinate;
import parts.cell.coordinate.CoordinateImpl;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class AppController {

    //Properties
    private SimpleBooleanProperty isFileSelectedProperty;
    private SimpleBooleanProperty isCellSelectedProperty;
    private IntegerProperty versionProperty;


    //Components
    @FXML private GridPane actionLine;
    @FXML private GridPane fileChooser;
    @FXML private ScrollPane table;
    @FXML private Button myButton=new Button();
    @FXML private Label myLabel ;
    @FXML private VBox commands;
    @FXML private VBox ranges;
    @FXML private HBox versionSelector;
    @FXML private Button updateCell;
    @FXML private Label currentVersionLabel;


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
    private Engine engine;

    @FXML
    private void initialize() {
        if (actionLineController != null && tableController != null && fileChooserController != null && versionSelectorController != null) {
            actionLineController.setMainController(this);
            tableController.setMainController(this);
            versionSelectorController.setMainController(this);
            fileChooserController.setMainController(this);
            commandsController.setMainController(this);
            rangesController.setMainController(this);

            isFileSelectedProperty = new SimpleBooleanProperty(false);
            isCellSelectedProperty = new SimpleBooleanProperty(false);
            versionProperty = new SimpleIntegerProperty(1);
            table.disableProperty().bind(isFileSelectedProperty.not());
            actionLine.disableProperty().bind(isFileSelectedProperty.not());
            versionSelector.disableProperty().bind(isFileSelectedProperty.not());
            currentVersionLabel.textProperty().bind(versionProperty.asString());

            engine = new EngineImpl();

        }
    }

//    private void setVersionSelectorOptions() {
//        // טוען את ה-DTO ומעדכן את הגרסאות בקונטרולר של הרכיב הקטן
////        VersionDTO versionDTO = new VersionDTO();
////        List<String> versions = versionDTO.getVersions();
//
//        int numOfVersions = engine.getNumOfVersions();
//        versionSelectorController.setVersionSelectorOptions(numOfVersions);
//        //versionSelectorController.setVersions(versions); // קריאה למתודה שמעדכנת את הגרסאות ברכיב הקטן
//    }

//    private void handleVersionSelection(String versionProperty) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Version Information");
//        alert.setHeaderText("Selected Version: " + versionProperty);
//        alert.setContentText("Details about " + versionProperty + " will appear here.");
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


//    public void onClickedCell(String coord) {
//        if (coordinate != null) {
//            tableController.removeFocusingOfCell(coordinate.toString());
//        }
//
//        coordinate = CoordinateImpl.parseCoordinate(coord);
//        CellDTO cell = engine.getCellDTOByCoordinate(coordinate);
//
//        actionLineController.setActionLine(cell);
//
//    }

    public void updateActionLine(Coordinate coord) {
        if (coord == null) {
            isCellSelectedProperty.set(false);
            actionLineController.setActionLine(null); // איפוס השורה
        } else {
            isCellSelectedProperty.set(true);
            CellDTO cell = engine.getCellDTOByCoordinate(coord);
            actionLineController.setActionLine(cell); // עדכון השורה עם התא החדש

        }
    }

//    public void SetFileSelected() {
//        isFileSelectedProperty.set(true);
//    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public File showFileSelector(FileChooser fileChooser) {
        return fileChooser.showOpenDialog(primaryStage);
    }


//    public void loadFile(String absolutePath, ProgressIndicator progressIndicator) throws Exception {
//        // יצירת משימה לטעינת הקובץ ברקע
//        Task<Void> loadFileTask = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                // סימולציה של התקדמות ההורדה
//                updateProgress(0, 100); // התחלת התקדמות ב-0%
//
//                // קריאת נתוני הקובץ (כאן אפשר להוסיף לולאה אם רוצים לסמלץ התקדמות)
//                engine.readFileData(absolutePath);
//
//                // ניתן להוסיף השהיה קטנה כדי לסמלץ התקדמות
//                for (int i = 0; i <= 100; i++) {
//                    Thread.sleep(30); // השהיה קצרה
//                    updateProgress(i, 100); // עדכון התקדמות
//                }
//
//                updateProgress(100, 100); // סיום התקדמות
//                return null;
//            }
//
//            @Override
//            protected void succeeded() {
//                super.succeeded();
//                // עדכון ה-UI במקרה של הצלחה
//                isFileSelectedProperty.set(true);
//                SheetDTO sheet = engine.getCurrentSheetDTO();
//                tableController.initializeGrid(sheet);
//
//                versionSelectorController.initializeVersionSelector(sheet.getVersion());
//                actionLineController.initializeActionLine(isCellSelectedProperty);
//
//                progressIndicator.setVisible(false); // הסתרת ProgressIndicator
//            }
//
//            @Override
//            protected void failed() {
//                super.failed();
//                // טיפול בשגיאה במקרה של כשל בטעינת הקובץ
//                System.out.println("Failed to load file: " + getException().getMessage());
//                progressIndicator.setVisible(false); // הסתרת ProgressIndicator
//            }
//        };
//
//        // קישור ה-ProgressIndicator להתקדמות המשימה
//        progressIndicator.progressProperty().bind(loadFileTask.progressProperty());
//        progressIndicator.setVisible(true);  // הצגת ה-ProgressIndicator
//
//        // הפעלת המשימה ב-Thread נפרד
//        new Thread(loadFileTask).start();
//    }


    public void updateCellValue(String value) {
        Coordinate coordinate = tableController.getCurrentlyFocusedCoord();

        Task<Void> updateCellTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                engine.updateCellValue(value, coordinate);
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                SheetDTO sheet = engine.getCurrentSheetDTO();
                CellDTO[][] cells = sheet.getCellsMatrix();

                Arrays.stream(cells).flatMap(Arrays::stream).forEach(cell -> {
                    if (cell != null) {
                        tableController.updateCellContent(cell.getCoord(), cell.getEffectiveValue());
                    }
                });

                tableController.addFocusingToCell(coordinate);
                actionLineController.setActionLine(engine.getCellDTOByCoordinate(coordinate));

                // עדכון אפשרויות הגרסה בצורה בטוחה
                versionProperty.set(sheet.getVersion());
                versionSelectorController.setVersionSelectorOptions(sheet.getVersion());
            }

            @Override
            protected void failed() {
                super.failed();
                System.out.println("Failed to update cell: " + getException().getMessage());
            }
        };

        new Thread(updateCellTask).start();
    }


    public CellDTO getCellDTO(String coord) {
        Coordinate coordinate = CoordinateImpl.parseCoordinate(coord);
        return engine.getCellDTOByCoordinate(coordinate);
    }

    public SheetDTO getSheetDTOByVersion(String version) {
        return engine.getSheetDTOByVersion(Integer.parseInt(version));
    }
    public void loadFileToSystem(String absolutePath) {
        try {
            engine.readFileData(absolutePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeComponentsAfterLoad() {

        isFileSelectedProperty.set(true);
        // עדכון ה-UI במקרה של הצלחה
        isFileSelectedProperty.set(true);
        SheetDTO sheet = engine.getCurrentSheetDTO();
        tableController.initializeGrid(sheet);

        versionSelectorController.initializeVersionSelector(sheet.getVersion());
        actionLineController.initializeActionLine(isCellSelectedProperty);
        commandsController.InitializeCommandsController(isCellSelectedProperty);

    }

    public void addRange(String rangeName, String rangeDefinition) throws Exception {
        engine.addRange(rangeName, rangeDefinition);
    }

    public void highlightRange(String rangeName) {
        List<Coordinate> rangeCoordinates = engine.getRangeCoordinates(rangeName);
        tableController.highlightRange(rangeCoordinates);
    }
}