package client.components.sheetManager.main;

import client.components.Utils.StageUtils;
import client.components.mainAppController.AppController;
import client.components.sheetManager.center.cellsTable.TableController;
import client.components.sheetManager.http.RequestsManager;
import client.components.sheetManager.left.commands.CommandsController;
import client.components.sheetManager.left.ranges.RangesController;
import client.components.sheetManager.skin.SkinSelectorController;
import client.components.sheetManager.top.actionLine.ActionLineController;
import client.components.sheetManager.top.fileChooser.FileChooserController;
import client.components.sheetManager.top.versions.VersionSelectorController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import shticell.engine.api.Engine;
import parts.cell.CellDTO;
import shticell.engine.impl.EngineImpl;
import parts.SheetDTO;
import shticell.engine.impl.SheetEngine;
import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import shticell.sheets.sheet.parts.cell.coordinate.CoordinateImpl;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;


import java.io.File;
import java.util.*;

public class SheetManagerController{

    private Stage primaryStage;
    private Scene scene;
    private int count;
    private Coordinate coordinate;
    private Engine engine;
    private String sheetName="beginner";
    private AppController mainController;
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
            bindUIComponents();
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
        rowSelected=new SimpleBooleanProperty(false);
        cellSelected = new SimpleBooleanProperty(false);
        rangeSelected = new SimpleBooleanProperty(false);
        showWhatIfMode = new SimpleBooleanProperty(false);
    }

    private void bindUIComponents() {
        BooleanBinding whatIfAndFileBinding = Bindings.or(fileSelectedProperty.not(), showWhatIfMode);

        table.disableProperty().bind(fileSelectedProperty.not());
        // actionLine.disableProperty().bind(fileSelectedProperty.not());
        versionSelector.disableProperty().bind(fileSelectedProperty.not());
        commands.disableProperty().bind(fileSelectedProperty.not());
        //ranges.disableProperty().bind(fileSelectedProperty.not());
        currentVersionLabel.textProperty().bind(versionProperty.asString());

        //what to disable in whatIfMode
        actionLine.disableProperty().bind(whatIfAndFileBinding);
        ranges.disableProperty().bind(whatIfAndFileBinding);
    }

    public void initializeComponentsAfterLoad() {

        clearSelectionStates();
        fileSelectedProperty.set(true);
        // עדכון ה-UI במקרה של הצלחה
        SheetDTO sheet = engine.getCurrentSheetDTO();
        SheetDTO sheet = requestsManager.getSheetDTO(sheetName);
        tableController.initializeGrid(sheet);
        versionSelectorController.initializeVersionSelector(sheet.getVersion());
        actionLineController.initializeActionLine(cellSelected);
        commandsController.InitializeCommandsController(cellSelected,rangeSelected, columnSelected,rowSelected,showWhatIfMode);
        rangesController.initializeRangesController(sheet.getRangesNames(), rangeSelected);

        versionProperty.set(1);
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

    public void updateCellValue(String value) {
        try {
            Coordinate coordinate = tableController.getCurrentlyFocusedCoord();
            tableController.removeMarksOfFocusedCell(); //temp - bette to do only if updated but insie it takes the updated cell from engine
            boolean isUpdated = engine.updateCellValue(value, coordinate);

            if (isUpdated) {
                SheetDTO sheet = engine.getCurrentSheetDTO();
                setCells(sheet);
                actionLineController.setActionLine(engine.getCellDTOByCoordinate(coordinate));
                setVersion(sheet.getVersion());

            }
            tableController.addMarksOfFocusingToCell(coordinate);
        }
        catch (Exception e) {
            StageUtils.showAlert("Error:", "Failed to update cell: " + e.getMessage());
            tableController.removeMarksOfFocusedCell();
            actionLineController.setActionLine(null);
            cellSelected.set(false);
        }
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


    public CellDTO getCellDTO(String coord) {
        Coordinate coordinate = CoordinateImpl.parseCoordinate(coord);
        return engine.getCellDTOByCoordinate(coordinate);
    }



    public SheetDTO getSheetDTOByVersion(String version) {
        return engine.getSheetDTOByVersion(Integer.parseInt(version));
    }
    public void loadFileToSystem(String absolutePath) throws Exception {
        engine.readFileData(absolutePath);
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
        tableController.clearCurrentHighlightRange();
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
        tableController.clearMarkOfCells();
    }


    public void handleCellClick(Coordinate coord)
    {
        if (coord == null) {
            cellSelected.set(false);
            actionLineController.setActionLine(null); // איפוס השורה
        }
        else {
            if(rangeSelected.get() || columnSelected.get() || rowSelected.get()) {
                columnSelected.set(false);
                rowSelected.set(false);
                rangeSelected.set(false);
                rangesController.clearSelectedRangeOption();
            }

            cellSelected.set(true);
            CellDTO cell = engine.getCellDTOByCoordinate(coord);
            actionLineController.setActionLine(cell); // עדכון השורה עם התא החדש
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

    // פונקציה לאיפוס כל הסימונים
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

    public SheetDTO filterData(Map<String,Set<EffectiveValue>> selectedValues, String rangeDefinition) {
        return engine.getFilteredSheetDTOFromMultipleCols(selectedValues, rangeDefinition);
    }

//    public Set<EffectiveValue> getDistinctValuesOfColInRange(String col,String rangeDefinition){
////        Set<String>strValues=new HashSet<String>();
//        Set<EffectiveValue> values=engine.getDistinctValuesOfColInRange(col,rangeDefinition);
////        strValues = values.stream()
////                .map(value -> tableController.calcValueToString(value)) // המרה למחרוזת//todo להוסיף את CALCVALUE למקום אחר
////                .collect(Collectors.toSet()); // המרת הזרם לסט
//        return values;
//    }

    public Map<String,Set<EffectiveValue>> getDistinctValuesOfMultipleColsInRange(List<Character> cols,String rangeDefinition){
        return engine.getDistinctValuesOfMultipleColsInRange(cols,rangeDefinition);
    }
    public void setEngineInWhatIfMode() throws IllegalStateException {
        engine.setEngineInWhatIfMode(tableController.getFocusedCoord());
    }
    public void calculateWhatIfValueForCell(double value){
        SheetDTO sheet = engine.calculateWhatIfValueForCell(value);
        setCells(sheet);


    }
    public void showCurrentSheet(){
        SheetDTO sheet=engine.getCurrentSheetDTO();
        setCells(sheet);
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


    public List<CellDTO> getColumnDataInRange(String rangeDefinition) {
        return engine.getColumnDataInRange(rangeDefinition);
    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }




    //public int getNumberOfColumns() {
    //     return engine.getNumberOfColumns();
    //}

//    public String[] createColumnsArray() {
//        int numOfColumns = engine.getNumberOfColumns();
//        String[] columnNames = new String[numOfColumns];
//        for (int i = 0; i < numOfColumns; i++) {
//            columnNames[i] = String.valueOf((char) ('A' + i));
//        }
//        return columnNames;
//    }
}