package components.MainComponent;

import components.left.commands.CommandsController;
import components.left.ranges.RangesController;
import components.top.actionLine.ActionLineController;
import components.center.cellsTable.TableController;
import components.top.fileChooser.FileChooserController;
import components.top.versions.VersionSelectorController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import parts.Engine;
import parts.cell.CellDTO;
import parts.EngineImpl;
import parts.SheetDTO;
import parts.sheet.cell.coordinate.Coordinate;
import parts.sheet.cell.coordinate.CoordinateImpl;
import parts.sheet.cell.expression.effectiveValue.EffectiveValue;


import java.io.File;
import java.util.*;

public class AppController {

    //Properties
    private SimpleBooleanProperty fileSelectedProperty;
    private IntegerProperty versionProperty;
    private SimpleBooleanProperty cellSelected;
    private SimpleBooleanProperty rangeSelected;
    private SimpleBooleanProperty columnSelected;



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

            fileSelectedProperty = new SimpleBooleanProperty(false);
            versionProperty = new SimpleIntegerProperty(1);
            columnSelected = new SimpleBooleanProperty(false);
            cellSelected = new SimpleBooleanProperty(false);
            rangeSelected = new SimpleBooleanProperty(false);

            table.disableProperty().bind(fileSelectedProperty.not());
            actionLine.disableProperty().bind(fileSelectedProperty.not());
            versionSelector.disableProperty().bind(fileSelectedProperty.not());
            commands.disableProperty().bind(fileSelectedProperty.not());
            ranges.disableProperty().bind(fileSelectedProperty.not());

            currentVersionLabel.textProperty().bind(versionProperty.asString());


            engine = new EngineImpl();

//            // Listener לבחירת טווח
//            rangeSelected.addListener((observable, oldValue, newValue) -> {
//                if (newValue) {
//                    // אם נבחר טווח, בטל את סימון התא הנבחר
//                    cellSelected.set(false);
//                    //clearBorderMarkOfCells();
//                }
//            });
//
//            // Listener לבחירת תא
//            cellSelected.addListener((observable, oldValue, newValue) -> {
//                if (newValue) {
//                    // אם נבחר תא, בטל את סימון הטווח הנבחר
//                    rangeSelected.set(false);
//                    //rangesController.clearCurrSelectedRange();
//                }
//            });

        }
    }

    public void initializeComponentsAfterLoad() {

        clearSelectionStates();
        fileSelectedProperty.set(true);
        // עדכון ה-UI במקרה של הצלחה
        fileSelectedProperty.set(true);
        SheetDTO sheet = engine.getCurrentSheetDTO();
        tableController.initializeGrid(sheet);
        versionSelectorController.initializeVersionSelector(sheet.getVersion());
        actionLineController.initializeActionLine(cellSelected);
        commandsController.InitializeCommandsController(cellSelected);
        rangesController.initializeRangesController(sheet.getRangesNames()/*, isCellSelectedProperty*/);
    }


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public File showFileSelector(FileChooser fileChooser) {
        return fileChooser.showOpenDialog(primaryStage);
    }

    public void updateCellValue(String value) {
        try {

            Coordinate coordinate = tableController.getCurrentlyFocusedCoord();
            tableController.removeMarksOfFocusedCell(); //temp - bette to do only if updated but insie it takes the updated cell from engine
            boolean isUpdated = engine.updateCellValue(value, coordinate);

            if (isUpdated) {
                SheetDTO sheet = engine.getCurrentSheetDTO();
                CellDTO[][] cells = sheet.getCellsMatrix();

                Arrays.stream(cells).flatMap(Arrays::stream).forEach(cell -> {
                    if (cell != null) {
                        tableController.updateCellContent(cell.getCoord(), cell.getEffectiveValue());
                    }
                });


                actionLineController.setActionLine(engine.getCellDTOByCoordinate(coordinate));

                // עדכון אפשרויות הגרסה בצורה בטוחה
                versionProperty.set(sheet.getVersion());
                versionSelectorController.setVersionSelectorOptions(sheet.getVersion());
            }
            tableController.addMarksOfFocusingToCell();
        }
        catch (Exception e) {
            showAlert("Error:", "Failed to update cell: " + e.getMessage());
        }

    }


    public CellDTO getCellDTO(String coord) {
        Coordinate coordinate = CoordinateImpl.parseCoordinate(coord);
        return engine.getCellDTOByCoordinate(coordinate);
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public SheetDTO getSheetDTOByVersion(String version) {
        return engine.getSheetDTOByVersion(Integer.parseInt(version));
    }
    public void loadFileToSystem(String absolutePath) {
        try {
            engine.readFileData(absolutePath);
        } catch (Exception e) {
            showAlert( "Error" ,e.getMessage());
        }
    }

    //Ranges
    public void addRange(String rangeName, String rangeDefinition) throws Exception {
        engine.addRange(rangeName, rangeDefinition);
    }

    public void highlightRange(String rangeName) {
        List<Coordinate> rangeCoordinates = engine.getRangeCoordinates(rangeName);
        tableController.highlightRange(rangeCoordinates);
    }

    public void clearCurrSelectedRangeHighlight() {
        //List<Coordinate> rangeCoordinates = engine.getRangeCoordinates(rangeName);
        tableController.clearCurrentHighlightRange();;
    }

    public void handleDeleteRange(String selectedRangeName) {

        rangeSelectedProperty().set(false); // עדכון מצב בחירת טווח
        tableController.clearCurrentHighlightRange(); //? this or remove marks
        engine.deleteRange(selectedRangeName);
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

    public List<String> getRanges() {
        return engine.getRangesNames();
    }

    public void resetCellStyle() {
        tableController.resetCellStyle();
    }

    public SheetDTO getSortedSheetDTO(String rangeDefinition, List<Character> columnsToSortBy) throws IllegalArgumentException {
        return engine.getSortedSheetDTO(rangeDefinition, columnsToSortBy);
    }

    public void clearBorderMarkOfCells()
    {
        //tableController.removeMarksFocusingOfFocusedCell();
        tableController.clearMarkOfCells();
    }


    public void handleCellClick(Coordinate coord)
    {
        if (coord == null) {
            cellSelected.set(false);
            actionLineController.setActionLine(null); // איפוס השורה
        }
        else {
            if(rangeSelected.get() || columnSelected.get()) {
                columnSelected.set(false);
                rangeSelected.set(false);
                rangesController.clearSelectedRangeOption();
            }

            cellSelected.set(true);
            CellDTO cell = engine.getCellDTOByCoordinate(coord);
            actionLineController.setActionLine(cell); // עדכון השורה עם התא החדש
        }
    }

//    public void updateActionLine(Coordinate coord) {
//
//        actionLineController.setActionLine(c); // איפוס השורה
//        } else {
//            isCellSelectedProperty.set(true);
//            CellDTO cell = engine.getCellDTOByCoordinate(coord);
//            actionLineController.setActionLine(cell); // עדכון השורה עם התא החדש
//
//        }
//    }

    public void handleRangeSelection(String rangeName) {
        // אם תא נבחר או עמודה נבחרה, בטל את הסימון שלהם
        if(cellSelected.get() || columnSelected.get()) {
            // בטל את הבחירה של התא והעמודה
            cellSelected.set(false);
            columnSelected.set(false);
            actionLineController.setActionLine(null); // איפוס השורה
        }

        clearBorderMarkOfCells(); // ניקוי סימוני תאים קודם
        highlightRange(rangeName);
        rangeSelectedProperty().set(true); // עדכון מצב בחירת טווח
    }

    public void handleColumnSelection() {
        // אם תא נבחר או טווח נבחר, בטל את הסימון שלהם
        if(cellSelected.get() || rangeSelected.get()) {
            cellSelected.set(false);
            rangeSelected.set(false);
            actionLineController.setActionLine(null); // איפוס השורה
        }

        //clearBorderMarkOfCells(); // ניקוי סימוני תאים קודם
        columnSelected.set(true); // עדכון מצב בחירת עמודה
    }
//
//    public void highlightColumn(String rangeName) {
//        List<Coordinate> rangeCoordinates = engine.getRangeCoordinates(rangeName);
//        tableController.highlightRange(rangeCoordinates);
//    }

    // פונקציה לאיפוס כל הסימונים
    public void clearSelectionStates() {
        cellSelected.set(false);
        rangeSelected.set(false);
        columnSelected.set(false);
    }


//    // פונקציות גישה למצב סימון תא
//    public boolean isCellSelected() {
//        return cellSelected.get();
//    }

//    public void setCellSelected(boolean isSelected) {
//        this.cellSelected.set(isSelected);
//    }

    public BooleanProperty cellSelectedProperty() {
        return cellSelected;
    }

//    // פונקציות גישה למצב סימון טווח
//    public boolean isRangeSelected() {
//        return rangeSelected.get();
//    }

//    public void setRangeSelected(boolean isSelected) {
//        this.rangeSelected.set(isSelected);
//    }

    public BooleanProperty rangeSelectedProperty() {
        return rangeSelected;
    }

//    // פונקציות גישה למצב סימון עמודה
//    public boolean isColumnSelected() {
//        return columnSelected.get();
//    }

//    public void setColumnSelected(boolean isSelected) {
//        this.columnSelected.set(isSelected);
//    }

    public BooleanProperty columnSelectedProperty() {
        return columnSelected;
    }


    public void filterData(Set<EffectiveValue> selectedValues) {

    }

    public Set<EffectiveValue> getDistinctValuesOfColInRange(String col,String rangeDefinition){
//        Set<String>strValues=new HashSet<String>();
        Set<EffectiveValue> values=engine.getDistinctValuesOfColInRange(col,rangeDefinition);
//        strValues = values.stream()
//                .map(value -> tableController.calcValueToString(value)) // המרה למחרוזת//todo להוסיף את CALCVALUE למקום אחר
//                .collect(Collectors.toSet()); // המרת הזרם לסט


        return values;
    }
}

//
//    public void setCellTextColor(Color color){
//        tableController.setCellTextColor(color);
//    }
//
//    public void setCellBorderColor(Color color) {
//        tableController.setCellBorderColor(color);
//    }

//    public void setCellBorderColor(Color color) {
//        String colorStr = formatColorToString(color);
//        tableController.setCellBorderColor(colorStr);
//    }