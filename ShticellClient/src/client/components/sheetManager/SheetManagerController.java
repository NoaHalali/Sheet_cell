package client.components.sheetManager;

import client.components.Utils.StageUtils;
import client.components.mainAppController.AppController;
import client.components.sheetManager.parts.center.cellsTable.TableController;
import client.components.sheetManager.http.RequestsManager;
import client.components.sheetManager.parts.left.commands.CommandsController;
import client.components.sheetManager.parts.left.ranges.RangesController;
import client.components.sheetManager.parts.skin.SkinSelectorController;
import client.components.sheetManager.parts.top.actionLine.ActionLineController;
import client.components.sheetManager.parts.top.fileChooser.FileChooserController;
import client.components.sheetManager.parts.top.versions.VersionSelectorController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import parts.cell.CellDTO;
import parts.SheetDTO;
import shticell.engines.sheetEngine.SheetEngine;
import shticell.permissions.PermissionType;
import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;


import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public class SheetManagerController {

    private Stage primaryStage;
    private Scene scene;
    private int count;
    private Coordinate coordinate;
    //private Engine engine;
    private SheetEngine sheetEngine;
    //private String sheetName = "beginner";
    private AppController mainController;
    //private final RequestsManager requestsManager = new RequestsManager(sheetName);
    private RequestsManager requestsManager;


    //Components
    @FXML private GridPane actionLine;
    @FXML private GridPane fileChooser;
    @FXML private ScrollPane table;
    @FXML private ScrollPane commands;
    @FXML private VBox ranges;
    @FXML private HBox versionSelector;
    @FXML private Label currentVersionLabel;
    @FXML private HBox skinSelector;
    @FXML private Button backToDashBoardButton;

    //Controllers
    @FXML private ActionLineController actionLineController;
    @FXML private TableController tableController;
    @FXML private FileChooserController fileChooserController;
    @FXML private CommandsController commandsController;
    @FXML private RangesController rangesController;
    @FXML private VersionSelectorController versionSelectorController;
    @FXML private SkinSelectorController skinSelectorController;

    //Properties
    private SimpleBooleanProperty fileSelectedProperty;
    private IntegerProperty versionProperty;
    private SimpleBooleanProperty cellSelected;
    private SimpleBooleanProperty rangeSelected;
    private SimpleBooleanProperty columnSelected;
    private SimpleBooleanProperty rowSelected;
    private SimpleBooleanProperty showWhatIfMode;

    @FXML
    private void initialize() {
        if (actionLineController != null && tableController != null && fileChooserController != null
                && versionSelectorController != null && commandsController != null && rangesController != null && skinSelectorController != null) {

            //engine = new SheetEngine();
            setMainControllerForComponents();
            initializeProperties();
            //bindUIComponents();
        }
    }

    private void setMainControllerForComponents() {
        actionLineController.setMainController(this);
        tableController.setMainController(this);
        versionSelectorController.setMainController(this);
        fileChooserController.setMainController(this);
        commandsController.setMainController(this);
        rangesController.setMainController(this);
        skinSelectorController.setMainController(this);
        skinSelectorController.setMainController(this);
    }

    private void initializeProperties() {
        fileSelectedProperty = new SimpleBooleanProperty(false);
        versionProperty = new SimpleIntegerProperty(1);
        columnSelected = new SimpleBooleanProperty(false);
        rowSelected = new SimpleBooleanProperty(false);
        cellSelected = new SimpleBooleanProperty(false);
        rangeSelected = new SimpleBooleanProperty(false);
        showWhatIfMode = new SimpleBooleanProperty(false);
    }

//    private void bindUIComponents() {
//        BooleanBinding whatIfAndFileBinding = Bindings.or(fileSelectedProperty.not(), showWhatIfMode);
//
//        table.disableProperty().bind(fileSelectedProperty.not());
//        // actionLine.disableProperty().bind(fileSelectedProperty.not());
//        versionSelector.disableProperty().bind(fileSelectedProperty.not());
//        commands.disableProperty().bind(fileSelectedProperty.not());
//        //ranges.disableProperty().bind(fileSelectedProperty.not());
//        currentVersionLabel.textProperty().bind(versionProperty.asString());
//
//        //what to disable in whatIfMode
//        actionLine.disableProperty().bind(whatIfAndFileBinding);
//        ranges.disableProperty().bind(whatIfAndFileBinding);
//    }

    private void bindUIComponents(BooleanBinding hasEditPermission) { //TODO - maybe need only the what if
        //BooleanBinding whatIfAndFileBinding = Bindings.or(fileSelectedProperty.not(), showWhatIfMode);

//        table.disableProperty().bind(fileSelectedProperty.not().or(hasEditPermission.not()));
//        versionSelector.disableProperty().bind(fileSelectedProperty.not());
//        commands.disableProperty().bind(fileSelectedProperty.not());
        currentVersionLabel.textProperty().bind(versionProperty.asString());

        // טווח השבתה במצב What-If והקובץ
        actionLine.disableProperty().bind(showWhatIfMode);
        ranges.disableProperty().bind(showWhatIfMode);

        // אפשר להוסיף Bindings נוספים לכפתורים או רכיבים אחרים כאן
    }


    public void initializeComponentsAfterLoad(String sheetName, BooleanBinding hasEditPermission) {

        clearSelectionStates();
        fileSelectedProperty.set(true);
        requestsManager = new RequestsManager(sheetName);
        bindUIComponents(hasEditPermission);

// שולחים את הבקשה לשרת ומעבירים את ה-Consumers המתאימים
        requestsManager.getSheetDTO(sheet -> {
            // פעולה במקרה של הצלחה: עדכון ה-UI
            tableController.initializeGrid(sheet);
            versionSelectorController.initializeVersionSelector(sheet.getVersion());
            actionLineController.initializeActionLine(cellSelected, hasEditPermission);
            commandsController.InitializeCommandsController(cellSelected, columnSelected, rowSelected, showWhatIfMode, hasEditPermission);
            rangesController.initializeRangesController(sheet.getRangesNames(), rangeSelected, hasEditPermission);
            versionProperty.set(1);

        }, errorMessage -> {
            // פעולה במקרה של כשל
            System.out.println("Error to get sheetDTO: " + errorMessage);
            StageUtils.showAlert("Error to get sheetDTO", errorMessage);
        });
    }

    public void updateCellValue(String value) {

        Coordinate coordinate = tableController.getFocusedCoord();

        // הסרת סימונים לפני עדכון התא
        tableController.removeMarksOfFocusedCell(() -> {
            // לאחר הסרת הסימונים, נבצע את עדכון התא
            requestsManager.updateCell(coordinate.toString(), value, isUpdated -> {
                if (isUpdated) {
                    // אם התא עודכן בהצלחה, נעדכן את ה-UI
                    updateUIComponentsAfterCellChange(coordinate);

                    // נוסיף סימונים חדשים לתא המעודכן
                    tableController.addMarksOfFocusingToCell(coordinate, null); // אין צורך ב-callback לאחר הפעולה
                }
            }, errorMessage -> {
                // במקרה של שגיאה, נטפל בה בהתאם
                StageUtils.showAlert("Error:", "Failed to update cell: " + errorMessage);
                tableController.removeMarksOfFocusedCell(null);
                actionLineController.setActionLine(null);
                cellSelected.set(false);
            });
        });
    }

    private void updateUIComponentsAfterCellChange(Coordinate coordinate) {
        requestsManager.getSheetDTO(sheet -> {
            // פעולה במקרה של הצלחה: עדכון ה-UI
            //SheetDTO sheet = engine.getCurrentSheetDTO();
            setCells(sheet);
            //actionLineController.setActionLine(engine.getCellDTOByCoordinate(coordinate));
            getCellDTO(coordinate, cell -> {
                actionLineController.setActionLine(cell);
            });
            setVersion(sheet.getVersion());

        }, errorMessage -> {
            StageUtils.showAlert("Error to get sheetDTO", errorMessage);
        });
    }


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        if (skinSelectorController != null) {
            skinSelectorController.setScene(scene);  // העברת הסצנה ל-SkinSelectorController
            skinSelectorController.initializeSkinSelector();  // אתחול ערכות הנושא
        }
    }

//    public void setEngine(Engine engine) {
//        this.engine = engine;
//        initializeComponentsAfterLoad();
//    }

    public File showFileChooser(FileChooser fileChooser) {
        return fileChooser.showOpenDialog(primaryStage);
    }


    private void setCells(SheetDTO sheet) {
        CellDTO[][] cells = sheet.getCellsMatrix();

        Arrays.stream(cells).flatMap(Arrays::stream).forEach(cell -> {
            if (cell != null) {
                tableController.updateCellContent(cell.getCoord(), cell.getEffectiveValue());
            }
        });
    }

    private void setVersion(int version) {
        versionProperty.set(version);
        versionSelectorController.setVersionSelectorOptions(version);
    }

    public void getCellDTO(Coordinate coord, Consumer<CellDTO> callback) {
        String coordStr = coord.toString();
        requestsManager.getCellDTO(coordStr, cellDTO -> {
            // מעבירים את התשובה מהשרת לקונטרולרים הקטנים
            callback.accept(cellDTO);
        }, errorMessage -> {
            StageUtils.showAlert("Error:", "Failed to get cell: " + errorMessage);
        });
    }
//    public List<String> getRanges() {
//        return engine.getRangesNames();
//    }


    public void getSheetDTOByVersion(String version, Consumer<SheetDTO> callback) {
        requestsManager.getSheetDtoByVersion(version, sheet -> {
            callback.accept(sheet);
        }, errorMessage -> {
            // פעולה במקרה של כשל
            System.out.println("Error to get sheetDTO: " + errorMessage);
            StageUtils.showAlert("Error to get sheetDTO", errorMessage);
        });
    }


//    public void loadFileToSystem(String absolutePath) throws Exception {
//        engine.readFileData(absolutePath);
//    }

    //Ranges
    public void addRange(String rangeName, String rangeDefinition,Consumer<List<String>> callBack) throws Exception {
        requestsManager.addRange(rangeName, rangeDefinition,rangeNames->{
            callBack.accept(rangeNames);
        },errorMessage -> {
            System.out.println("Error to get sheetDTO: " + errorMessage);
            StageUtils.showAlert("Error to get sheetDTO", errorMessage);
        });
    }

    public void highlightRange(String rangeName) {
        requestsManager.getRangeCoordinates(rangeName,rangeCoordinates->{
            Platform.runLater(() -> {
                tableController.highlightRange(rangeCoordinates);
            });
        },errorMessage -> {
            System.out.println("Error to get sheetDTO: " + errorMessage);
            StageUtils.showAlert("Error to get Range Coordinates", errorMessage);
        } );

    }

    public void clearCurrSelectedRangeHighlight() {
        //List<Coordinate> rangeCoordinates = engine.getRangeCoordinates(rangeName);
        tableController.clearCurrentHighlightRange();
    }

    public void handleDeleteRange(String rangeName,Consumer<List<String>> callBack) {

        rangeSelectedProperty().set(false); // עדכון מצב בחירת טווח
        tableController.clearCurrentHighlightRange(); //? this or remove marks
        requestsManager.deleteRange(rangeName ,rangeNames->{
            callBack.accept(rangeNames);
        },errorMessage -> {
            System.out.println("Error to get sheetDTO: " + errorMessage);
            StageUtils.showAlert("Error to get sheetDTO", errorMessage);
        });

    }

    public Map<String,String> getStylesFromMainSheet() {
        return tableController.getCoordToStyleMap();
    }


    public void setCellTextColor(Color color) {
        String colorStr = formatColorToString(color);
        tableController.setCellTextColor(colorStr);
    }


    public void setCellBackgroundColor(Color color) {
        String colorStr = formatColorToString(color);
        tableController.setCellBackgroundColor(colorStr);
    }
    public void changeWhatIfMode(boolean flag){
        showWhatIfMode.set(flag);
    }

    private String formatColorToString(Color color) {
        if (color != null) {
            // הפורמט כולל ערך עבור האדום, הירוק והכחול בלבד
            return String.format("#%02X%02X%02X",
                    (int) (color.getRed() * 255),
                    (int) (color.getGreen() * 255),
                    (int) (color.getBlue() * 255));
        }
        return "#000000"; // צבע ברירת מחדל אם הצבע הוא null
    }


    public void resetCellStyle() {
        tableController.resetCellStyle();
    }

    public void getSortedSheetDTO(String rangeDefinition, List<Character> columnsToSortBy, Consumer<SheetDTO> callback ) throws IllegalArgumentException {
        //return engine.getSortedSheetDTO(rangeDefinition, columnsToSortBy);
        requestsManager.getSortedSheetDTO(rangeDefinition, columnsToSortBy, sheet -> {
            callback.accept(sheet);
        }, errorMessage -> {
            // פעולה במקרה של כשל
            System.out.println("Error to get sheetDTO: " + errorMessage);
            StageUtils.showAlert("Error to get sheetDTO", errorMessage);
        });
    }

    public void clearBorderMarkOfCells() {
        tableController.clearMarkOfCells(() -> {
            // אפשר להוסיף כאן פעולה אם צריך לאחר סיום ניקוי הסימונים
            System.out.println("Cell borders cleared");
        });
    }

    public void handleCellClick(Coordinate coord)
    {
        if (coord == null) {
            cellSelected.set(false);
            actionLineController.setActionLine(null); // איפוס השורה
        }
        else {
            if (rangeSelected.get() || columnSelected.get() || rowSelected.get()) {
                columnSelected.set(false);
                rowSelected.set(false);
                rangeSelected.set(false);
                rangesController.clearSelectedRangeOption();
            }

            cellSelected.set(true);
            //CellDTO cell = engine.getCellDTOByCoordinate(coord);
            requestsManager.getCellDTO(coord.toString(), cell -> {
                actionLineController.setActionLine(cell);
            }, errorMessage -> {
                StageUtils.showAlert("Error:", "Failed to get cell: " + errorMessage);
            });
        }
    }

    public void handleRangeSelection(String rangeName) {
        // אם תא נבחר או עמודה נבחרה, בטל את הסימון שלהם
        if(cellSelected.get() || columnSelected.get()||rowSelected.get()) {
            // בטל את הבחירה של התא והעמודה
            cellSelected.set(false);
            columnSelected.set(false);
            rowSelected.set(false);
            actionLineController.setActionLine(null); // איפוס השורה
        }

        clearBorderMarkOfCells(); // ניקוי סימוני תאים קודם
        highlightRange(rangeName);
        rangeSelectedProperty().set(true); // עדכון מצב בחירת טווח
    }

    public void handleColumnSelection() {
        // אם תא נבחר או טווח נבחר, בטל את הסימון שלהם
        if(cellSelected.get() || rangeSelected.get()||rowSelected.get()) {
            cellSelected.set(false);
            rangeSelected.set(false);
            rowSelected.set(false);
            actionLineController.setActionLine(null); // איפוס השורה
        }

        //clearBorderMarkOfCells(); // ניקוי סימוני תאים קודם
        columnSelected.set(true); // עדכון מצב בחירת עמודה
    }

    public Coordinate getCurrentlyFocusedCellCoord() {
        return tableController.getCurrentlyFocusedCoord();
    }

    public void handleRowSelection() {
        // If a cell or range is selected, clear their selection
        if (cellSelected.get() || columnSelected.get() || rangeSelected.get()) {
            cellSelected.set(false);
            columnSelected.set(false);
            rangeSelected.set(false);
            actionLineController.setActionLine(null); // Reset the action line
        }

        rowSelected.set(true); // Update the row selection state
    }

    public void clearSelectionStates() {
        cellSelected.set(false);
        rangeSelected.set(false);
        columnSelected.set(false);
        rowSelected.set(false);
        showWhatIfMode.set(false);
    }

    public BooleanProperty cellSelectedProperty() {
        return cellSelected;
    }


    public BooleanProperty rangeSelectedProperty() {
        return rangeSelected;
    }


    public void getDistinctValuesOfMultipleColsInRange(List<Character> cols, String rangeDefinition, Consumer<Map<String, Set<EffectiveValue>>> onSuccess, Consumer<String> onFailure) {
        //return engine.getDistinctValuesOfMultipleColsInRange(cols,rangeDefinition);
        // קריאה ל-requestsManager עם צרכנים (Consumers) שמטפלים בתוצאה ובשגיאות
        requestsManager.getDistinctValuesOfMultipleColsInRange(cols, rangeDefinition, distinctValuesMap -> {
            // קריאה ל-onSuccess עם המפה שהתקבלה אם הפעולה הצליחה
            onSuccess.accept(distinctValuesMap);
        }, errorMessage -> {
            // קריאה ל-onFailure במקרה של שגיאה
            onFailure.accept(errorMessage);
            StageUtils.showAlert("Error to get distinct values", errorMessage);
        });
    }


    public void filterData(Map<String,Set<EffectiveValue>> selectedValues, String rangeDefinition, Consumer<SheetDTO> onSuccess, Consumer<String> onFailure) {
        //return engine.getFilteredSheetDTOFromMultipleCols(selectedValues, rangeDefinition);
        requestsManager.getFilteredSheetDTOFromMultipleCols(selectedValues, rangeDefinition, sheet -> {
            // קריאה ל-onSuccess עם הגיליון שהתקבל אם הפעולה הצליחה
            onSuccess.accept(sheet);
        }, errorMessage -> {
            // קריאה ל-onFailure במקרה של שגיאה
            onFailure.accept(errorMessage);
            StageUtils.showAlert("Error to get filtered sheet", errorMessage);
        });

    }

    public void setEngineInWhatIfMode(Coordinate coord, Consumer<Void> onSuccess, Consumer<String> onFailure)throws IllegalArgumentException{
        requestsManager.setEngineInWhatIfMode(coord, success -> {
            onSuccess.accept(null); // קריאה ל-onSuccess אם הצליחה הבקשה
        }, failure -> {
            onFailure.accept(failure); // קריאה ל-onFailure אם נכשלה הבקשה
        });
    }

    public void calculateWhatIfValueForCell(double value) {
        // קריאה אסינכרונית ל-requestsManager כדי לחשב ערך What If
        requestsManager.calculateWhatIfValueForCell(value, sheet -> {
            // הפעלה של הקוד ב-Thread הראשי עם Platform.runLater לאחר קבלת התשובה
            Platform.runLater(() -> {
                setCells(sheet);
            });
        }, errorMessage -> {
            // טיפול במקרה של שגיאה
            Platform.runLater(() -> {
                StageUtils.showAlert("Error", errorMessage);
            });
        });
    }

    public void showCurrentSheet(){
        requestsManager.getSheetDTO(sheet -> {
            // פעולה במקרה של הצלחה: עדכון ה-UI
            setCells(sheet);
        }, errorMessage -> {
            // פעולה במקרה של כשל
            System.out.println("Error to get sheetDTO: " + errorMessage);
            StageUtils.showAlert("Error to get sheetDTO", errorMessage);
        });

    }

    public void setColumnWidth(int width) throws IllegalArgumentException {
        tableController.setColumnWidth(width);
    }

    public void setRowHeight(int height) {
        tableController.setRowHeight(height);
    }

    public void setColumnAlignment(String alignment) {
        tableController.setColumnAlignment(alignment);
    }

    public int getFocusedRowHeight() {
        return tableController.getFocusedRowHeight();
    }

    public int getFocusedColumnWidth() {
        return tableController.getFocusedColumnWidth();
    }


    public void getColumnDataInRange(String rangeDefinitioncallback ,Consumer<List<CellDTO>> callBack) {
        requestsManager.getColumnDataInRange(rangeDefinitioncallback, columnData -> {
            callBack.accept(columnData);
        }, errorMessage -> {
            StageUtils.showAlert("Error to get column data", errorMessage);
        });
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void handleBackToDashBoardButtonClick() {
        mainController.switchToMultiSheetsScreen();
    }
}