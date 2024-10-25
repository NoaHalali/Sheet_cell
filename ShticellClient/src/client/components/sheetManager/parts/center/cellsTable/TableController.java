package client.components.sheetManager.parts.center.cellsTable;

import client.components.Utils.StageUtils;

import client.components.sheetManager.parts.center.cell.CellController;
import client.components.sheetManager.SheetManagerController;
import javafx.application.Platform;
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

import static client.components.Utils.Constants.CELL_FXML_RESOURCE_LOCATION;

public class TableController {
    private final String basicCellStyle = "-fx-background-color: #f0f0f0; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 0.5px;";
    private SheetManagerController parentController;
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(CELL_FXML_RESOURCE_LOCATION));
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

    public void setParentController(SheetManagerController parentController) {
        this.parentController = parentController;
    }

private void handleCellClick(String newCoord) {
    boolean isNewCoordSelected = newCoord != null;
    boolean isDoubleClick = currentlyFocusedCoord != null && currentlyFocusedCoord.toString().equals(newCoord);

    if (isDoubleClick) {
        removeMarksOfFocusedCell(() -> {
            parentController.handleCellClick(null); // ביטול בחירת תא לאחר הסרת הסימונים
        });
    } else if (isNewCoordSelected) {
        clearMarkOfCells(() -> {
            addMarksOfFocusingToCell(CoordinateImpl.parseCoordinate(newCoord), () -> {
                setFocusedCoord(CoordinateImpl.parseCoordinate(newCoord)); // עדכון הקואורדינטה הממוקדת
                parentController.handleCellClick(currentlyFocusedCoord); // עדכון בחירת תא לאחר הוספת סימונים
            });
        });
    }
}

    public void clearMarkOfCells(Runnable onComplete) {
        // ניקוי כל הסימונים בתאים (בהתאם למפה שלך)
        for (Map.Entry<String, CellController> entry : coordToCellControllerMap.entrySet()) {
            CellController cellController = entry.getValue();
            if (cellController != null) {
                cellController.resetBorder(); // הסרת הסימון
            }
        }

        if (onComplete != null) {
            onComplete.run();
        }
    }

    public void removeMarksOfFocusedCell(Runnable onComplete) {
        if (currentlyFocusedCellController != null) {
            currentlyFocusedCellController.resetBorder();

            parentController.getCellDTO(currentlyFocusedCoord, cell -> {
                Platform.runLater(() -> {
                    List<Coordinate> dependsOn = cell.getDependsOn();
                    for (Coordinate coord : dependsOn) {
                        CellController depCellController = coordToCellControllerMap.get(coord.toString());
                        if (depCellController != null) {
                            depCellController.resetBorder();
                        }
                    }

                    List<Coordinate> influencingOn = cell.getInfluencingOn();
                    for (Coordinate coord : influencingOn) {
                        CellController infCellController = coordToCellControllerMap.get(coord.toString());
                        if (infCellController != null) {
                            infCellController.resetBorder();
                        }
                    }
                    setFocusedCoord(null);

                    // קריאה ל-callback כשהפעולה הסתיימה
                    if (onComplete != null) {
                        onComplete.run();
                    }
                });
            });
        }
    }

    public void addMarksOfFocusingToCell(Coordinate newFocusedCoord, Runnable onComplete) {
        setFocusedCoord(newFocusedCoord); // עדכון הקואורדינטה הממוקדת
        currentlyFocusedCellController.setBorder("red", "3px"); // סימון הגבול של התא הנוכחי

        // בקשה אסינכרונית ל-CellDTO עבור התא הממוקד
        parentController.getCellDTO(currentlyFocusedCoord, cell -> {
            Platform.runLater(() -> {
                // סימון התאים שתלויים בתא הממוקד
                List<Coordinate> dependsOn = cell.getDependsOn();
                for (Coordinate coord : dependsOn) {
                    CellController depCellController = coordToCellControllerMap.get(coord.toString());
                    if (depCellController != null) {
                        depCellController.setBorder("#4d66cc", "2px");
                    }
                }

                // סימון התאים שמשפיעים על התא הממוקד
                List<Coordinate> influencingOn = cell.getInfluencingOn();
                for (Coordinate coord : influencingOn) {
                    CellController infCellController = coordToCellControllerMap.get(coord.toString());
                    if (infCellController != null) {
                        infCellController.setBorder("#669966", "2px");
                    }
                }

                // קריאה ל-onComplete כשהסימונים הסתיימו
                if (onComplete != null) {
                    onComplete.run();
                }
            });
        });
    }

    private void handleColumnClick(int colIndex) {
        clearMarkOfCells(() -> {
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
            parentController.handleColumnSelection();
        });
    }

    private void handleRowClick(int rowIndex){
            clearMarkOfCells(() -> {
                currentRowIndex = rowIndex;
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

                parentController.handleRowSelection(); // Notify the main controller about the row selection
            });
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
        clearMarkOfCells(() -> {
            for (Coordinate coord : rangeCoordinates) {
                CellController cellController = coordToCellControllerMap.get(coord.toString());
                cellController.setRangeHighlight();
            }
            currentlyHighlightedRange = rangeCoordinates;
        });
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
