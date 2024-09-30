package components.center.cellsTable;

import components.MainComponent.AppController;
import components.Utils.StageUtils;
import components.center.cell.CellController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import parts.cell.CellDTO;
import parts.SheetDTO;
import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import shticell.sheets.sheet.parts.cell.coordinate.CoordinateImpl;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableController {
    private final String basicCellStyle = "-fx-background-color: #f0f0f0; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 0.5px;";
    private AppController mainController;
    @FXML private GridPane dynamicGridPane;
    private final Map<String, CellController> coordToCellControllerMap = new HashMap<>();
    private Coordinate currentlyFocusedCoord; // שדה לשמירת הקואורדינטה הממוקדת הנוכחית
    private CellController currentlyFocusedCellController; // שדה לשמירת התא הממוקד הנוכחי
    private List<Coordinate> currentlyHighlightedRange;
    private List<Coordinate> currentlyHighlightedColumn; // רשימת התאים בעמודה הממוקדת הנוכחית
    private int currentColumnIndex;
    private int currentRowIndex;
    private List<Coordinate> currentlyHighlightedRow;

    public void initializeGrid(SheetDTO sheet) {
        setupGrid(sheet);
        populateGridWithCells(sheet, true);
        addRowAndColumnLabels(sheet.getNumberOfRows(), sheet.getNumberOfCols(),true);
    }

    public void showSheetPreview(SheetDTO sheet) {
        setupGrid(sheet);
        populateGridWithCells(sheet, false);
        addRowAndColumnLabels(sheet.getNumberOfRows(), sheet.getNumberOfCols(),false);
    }

    private void setupGrid(SheetDTO sheet) {
        dynamicGridPane.getColumnConstraints().clear();
        dynamicGridPane.getRowConstraints().clear();
        dynamicGridPane.getChildren().clear();

        for (int col = 0; col <= sheet.getNumberOfCols(); col++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPrefWidth(sheet.getColumnWidth());
            dynamicGridPane.getColumnConstraints().add(colConst);
        }

        for (int row = 0; row <= sheet.getNumberOfRows(); row++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPrefHeight(sheet.getRowHeight());
            dynamicGridPane.getRowConstraints().add(rowConst);
        }
    }

    private void populateGridWithCells(SheetDTO sheet, boolean enableClick) {

        for (int row = 1; row <= sheet.getNumberOfRows(); row++) {
            for (int col = 1; col <= sheet.getNumberOfCols(); col++) {
                CoordinateImpl coord = new CoordinateImpl(row, col);
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/center/cell/cell.fxml"));
                    BorderPane cellPane = loader.load();
                    CellController cellController = loader.getController();

                    CellDTO cell = sheet.getCell(coord);
                    cellController.setCoord(cell.getCoord());

                    String cellText = cell == null ? "" : calcValueToPrint(cell.getEffectiveValue());
                    cellController.setText(cellText);
                    cellController.setInitialStyle();

                    if (enableClick) {
                        String coordStr = coord.toString();
                        cellPane.setOnMouseClicked(event -> handleCellClick(coordStr));

                        cellController.applyHoverEffectListeners();
                    }

                    dynamicGridPane.add(cellPane, col, row);
                    coordToCellControllerMap.put(coord.toString(), cellController);
                } catch (IOException e) {
                    StageUtils.showAlert("Error", e.getMessage());
                }
            }
        }
    }

    private void addRowAndColumnLabels(int rows, int cols, boolean enableClick) {
        for (int row = 1; row <= rows; row++) {
            Label label = new Label(String.valueOf(row));
            label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            label.setStyle("-fx-background-color: #f0f0f0; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 0.5px;");
            dynamicGridPane.add(label, 0, row);
            if(enableClick) {
                final int rowIndex = row; // use final for lambda
                label.setOnMouseClicked(event -> handleRowClick(rowIndex)); // Add row click handler
            }
        }

        for (int col = 1; col <= cols; col++) {
            Label label = new Label(String.valueOf((char) ('A' + col - 1)));
            label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            label.setStyle("-fx-background-color: #f0f0f0; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 0.5px;");
            dynamicGridPane.add(label, col, 0);
            if (enableClick) {
            final int colIndex = col; // use final for lambda
            label.setOnMouseClicked(event -> handleColumnClick(colIndex)); // Add column click handler
            }
        }
    }


    public void updateCellContent(Coordinate coord, EffectiveValue newText) {
        CellController cellController = coordToCellControllerMap.get(coord.toString());
        if (cellController != null) {
            cellController.setText(calcValueToPrint(newText));
        }
    }

    public Map<String, String> getCoordToStyleMap() {
        Map<String, String> styleMap = new HashMap<String, String>();

        // לולאה עבור כל CellController ב- coordToCellControllerMap
        for (CellController cellController : coordToCellControllerMap.values()) {
            String coordStr = cellController.getCoord().toString();
            String style = cellController.copyPreviewStyle();

            styleMap.put(coordStr, style);
        }
        styleMap.put(CoordinateImpl.notExists.toString(),basicCellStyle);

        return styleMap;
    }

    public void updateCellsStyleAfterSort(Map<String, String> prevStyleMap, SheetDTO sheet){
        for (int row = 1; row <= sheet.getNumberOfRows(); row++) {
            for (int col = 1; col <= sheet.getNumberOfCols(); col++) {
                CoordinateImpl coord = new CoordinateImpl(row, col);

                CellController cell = coordToCellControllerMap.get(coord.toString()); // current coordinate controller after sort
                Coordinate prevCoord = sheet.getCell(coord).getCoord(); //previous coordinate of this cell, with its style
                String prevCoordStyle = prevStyleMap.get(prevCoord.toString());//get the old style of our current cell
                cell.setCellStyle(prevCoordStyle); //set cell style to its new place after sort
            }
        }
    }

    public String calcValueToPrint(EffectiveValue effectiveValue) {
        if (effectiveValue == null) {
            return "";
        }
        switch (effectiveValue.getCellType()) {
            case NUMERIC:
                double num = effectiveValue.extractValueWithExpectation(Double.class);
                if (Double.isNaN(num) || Double.isInfinite(num)) return "NaN";
                return num == Math.floor(num) ? String.format("%,d", (long) num) : String.format("%,.2f", num);
            case STRING:
                return effectiveValue.extractValueWithExpectation(String.class).trim();
            case BOOLEAN:
                return String.valueOf(effectiveValue.extractValueWithExpectation(Boolean.class)).toUpperCase();
            default:
                throw new IllegalArgumentException();
        }
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    private void handleCellClick(String newCoord) {

        boolean isNewCoordSelected = newCoord != null;
        boolean isDoubleClick = currentlyFocusedCoord != null && currentlyFocusedCoord.toString().equals(newCoord);

        if (isDoubleClick) {
            removeMarksOfFocusedCell();
            mainController.handleCellClick(null); // ביטול בחירת תא

        } else if (isNewCoordSelected) {
            clearMarkOfCells(); // ניקוי הדגשות תאים ישנות
            addMarksOfFocusingToCell(CoordinateImpl.parseCoordinate(newCoord));

            mainController.handleCellClick(currentlyFocusedCoord); // עדכון בחירת תא
        }
    }



    private void handleColumnClick(int colIndex) {
        clearMarkOfCells();
        currentColumnIndex = colIndex;
        currentlyHighlightedColumn = new ArrayList<>();

        // עבור על כל התאים בעמודה עם האינדקס שנבחר
        //TODO - maybe get the column from the engine
        for (int row = 1; row <= dynamicGridPane.getRowConstraints().size(); row++) {
            CoordinateImpl coord = new CoordinateImpl(row, colIndex);
            CellController cellController = coordToCellControllerMap.get(coord.toString());

            if (cellController != null) {
                cellController.setBorder("#FFA500", "2px"); // הדגש את התא בצבע כתום
                currentlyHighlightedColumn.add(coord); // הוסף את התא לרשימת התאים בעמודה הנבחרת
            }
        }
        mainController.handleColumnSelection();
    }

    private void handleRowClick(int rowIndex) {
        clearMarkOfCells();

        currentRowIndex=rowIndex;
        currentlyHighlightedRow = new ArrayList<>();

        // Highlight all cells in the selected row
        for (int col = 1; col <= dynamicGridPane.getColumnConstraints().size(); col++) {
            CoordinateImpl coord = new CoordinateImpl(rowIndex, col);
            CellController cellController = coordToCellControllerMap.get(coord.toString());

            if (cellController != null) {
                cellController.setBorder("#FFA500", "2px"); // Highlight the cell with orange
                currentlyHighlightedRow.add(coord); // Add the cell to the highlighted row list
            }
        }

        mainController.handleRowSelection(); // Notify the main controller about the row selection
    }


    public void addMarksOfFocusingToCell(Coordinate newFocusedCoord) {
        setFocusedCoord(newFocusedCoord);
        currentlyFocusedCellController.setBorder("red", "3px");

        CellDTO cell = mainController.getCellDTO(currentlyFocusedCoord.toString());
        List<Coordinate> dependsOn = cell.getDependsOn();
        for (Coordinate coord : dependsOn) {
            CellController depCellController = coordToCellControllerMap.get(coord.toString());
            depCellController.setBorder("#4d66cc", "2px");
        }

        List<Coordinate> influencingOn = cell.getInfluencingOn();
        for (Coordinate coord : influencingOn) {
            CellController infCellController = coordToCellControllerMap.get(coord.toString());
            infCellController.setBorder("#669966", "2px");
        }

    }

    public void removeMarksOfFocusedCell() {
        if (currentlyFocusedCellController != null) {
            currentlyFocusedCellController.resetBorder();
            Coordinate cellCoord = currentlyFocusedCellController.getCoord();
            CellDTO cell = mainController.getCellDTO(cellCoord.toString());
            List<Coordinate> dependsOn = cell.getDependsOn();

            for (Coordinate coord : dependsOn) {
                CellController depCellController = coordToCellControllerMap.get(coord.toString());
                depCellController.resetBorder();
            }

            List<Coordinate> influencingOn = cell.getInfluencingOn();
            for (Coordinate coord : influencingOn) {
                CellController infCellController = coordToCellControllerMap.get(coord.toString());
                infCellController.resetBorder();
            }
            setFocusedCoord(null);
        }
    }

    public void setFocusedCoord(Coordinate coord) {
        currentlyFocusedCoord = coord;
        if (coord != null) {
            currentlyFocusedCellController = coordToCellControllerMap.get(coord.toString());
        } else {
            currentlyFocusedCellController = null;
        }
    }

    public Coordinate getCurrentlyFocusedCoord() {
        return currentlyFocusedCoord;
    }

    public void highlightRange(List<Coordinate> rangeCoordinates) {
        clearMarkOfCells();
        for (Coordinate coord : rangeCoordinates) {
            CellController cellController = coordToCellControllerMap.get(coord.toString());
            cellController.setRangeHighlight();
            //cellController.setBackgroundColor("red");
        }
        currentlyHighlightedRange = rangeCoordinates;
    }

    public void clearCurrentHighlightRange() {
        if (currentlyHighlightedRange != null) {
            for (Coordinate coord : currentlyHighlightedRange) {
                CellController cellController = coordToCellControllerMap.get(coord.toString());
                cellController.clearRangeHighlight();
                //cellController.setBackgroundColor("#f0f0f0");
            }
            currentlyHighlightedRange = null;
        }
    }

    public void setCellTextColor(String colorStr){
        if (currentlyFocusedCellController != null) {
            currentlyFocusedCellController.setTextColor(colorStr);
        }
    }

    public void setCellBackgroundColor(String colorStr) {
        if (currentlyFocusedCellController != null) {
            currentlyFocusedCellController.setBackgroundColor(colorStr);
        }
    }

    public void resetCellStyle() {
        if (currentlyFocusedCellController != null) {
            currentlyFocusedCellController.resetStyle();
        }
    }

    public void clearMarkOfCells() {
        clearCurrentHighlightRange();
        removeMarksOfFocusedCell();
        clearColumnHighlight();
        clearRowHighlight();

    }
    public void clearRowHighlight() {
        if (currentlyHighlightedRow != null) {
            for (Coordinate coord : currentlyHighlightedRow) {
                CellController cellController = coordToCellControllerMap.get(coord.toString());
                if (cellController != null) {
                    cellController.resetBorder(); // Reset the border of the cell
                }
            }
            currentlyHighlightedRow = null; // Clear the list of highlighted row cells
            currentRowIndex=-1;
        }

    }

    private void clearColumnHighlight() {
        if (currentlyHighlightedColumn != null) {
            for (Coordinate coord : currentlyHighlightedColumn) {
                CellController cellController = coordToCellControllerMap.get(coord.toString());
                if (cellController != null) {
                    cellController.resetBorder(); // איפוס גבול התא
                }
            }
            currentlyHighlightedColumn = null; // נקה את רשימת התאים בעמודה הנבחרת
            currentColumnIndex = -1; // נקה את האינדקס של העמודה הנבחרת
        }
    }

    public void setRowHeight(int height) {
//        if (height <= 0) {
//            throw new IllegalArgumentException("Width must be greater than 0");
//        }
        RowConstraints rowConst = dynamicGridPane.getRowConstraints().get(currentRowIndex);
        rowConst.setPrefHeight(height);

    }

    public void setColumnWidth(int width) throws IllegalArgumentException {
//        if(width <=0 ){
//            throw new IllegalArgumentException("Width must be greater than 0");
//        }
        ColumnConstraints colConst = dynamicGridPane.getColumnConstraints().get(currentColumnIndex);
        colConst.setPrefWidth(width);
    }

    public void setColumnAlignment(String alignment) {
        if (currentlyHighlightedColumn != null) {
            for (Coordinate coord : currentlyHighlightedColumn) {
                CellController cellController = coordToCellControllerMap.get(coord.toString());
                cellController.setAlignment(alignment);
            }
        }
    }

    public int getFocusedRowHeight() {
        return (int) dynamicGridPane.getRowConstraints().get(currentRowIndex).getPrefHeight();
    }

    public int getFocusedColumnWidth() {
        return (int) dynamicGridPane.getColumnConstraints().get(currentColumnIndex).getPrefWidth();
    }
    public Coordinate getFocusedCoord(){
        return currentlyFocusedCoord;
    }

}
