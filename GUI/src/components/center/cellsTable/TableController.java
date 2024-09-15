package components.center.cellsTable;

import components.MainComponent.AppController;
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
import parts.sheet.cell.coordinate.Coordinate;
import parts.sheet.cell.coordinate.CoordinateImpl;
import parts.sheet.cell.expression.effectiveValue.EffectiveValue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableController {

    private AppController mainController;
    @FXML private GridPane dynamicGridPane;
    private final Map<String, CellController> coordToCellControllerMap = new HashMap<>();
    private Coordinate currentlyFocusedCoord; // שדה לשמירת הקואורדינטה הממוקדת הנוכחית
    private CellController currentlyFocusedCellController; // שדה לשמירת התא הממוקד הנוכחי
    private List<Coordinate> currentlyHighlightedRange;

    public void initializeGrid(SheetDTO sheet) {
        setupGrid(sheet);
        populateGridWithCells(sheet, true);
        addRowAndColumnLabels(sheet.getNumberOfRows(), sheet.getNumberOfCols());
    }

    public void showSheetPreview(SheetDTO sheet) {
        setupGrid(sheet);
        populateGridWithCells(sheet, false);
        addRowAndColumnLabels(sheet.getNumberOfRows(), sheet.getNumberOfCols());
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



                        cellController.setBackgroundColor("#f0f0f0");
                        cellController.setAlignment("center");
                        cellController.setBorderColor("black");
                        cellController.setBorderWidth("0.5px");


                    if (enableClick) {
                        String coordStr = coord.toString();
                        cellPane.setOnMouseClicked(event -> handleCellClick(coordStr));

                        cellController.applyHoverEffectListeners();
                    }


                    dynamicGridPane.add(cellPane, col, row);
                    coordToCellControllerMap.put(coord.toString(), cellController);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addRowAndColumnLabels(int rows, int cols) {
        for (int row = 1; row <= rows; row++) {
            Label label = new Label(String.valueOf(row));
            label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            label.setStyle("-fx-background-color: #f0f0f0; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 0.5px;");
            dynamicGridPane.add(label, 0, row);
        }
        for (int col = 1; col <= cols; col++) {
            Label label = new Label(String.valueOf((char) ('A' + col - 1)));
            label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            label.setStyle("-fx-background-color: #f0f0f0; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 0.5px;");
            dynamicGridPane.add(label, col, 0);
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

//        for (String coordStr : coordToCellControllerMap.keySet()) {
//            CellController cellController = coordToCellControllerMap.get(coordStr);
//            styleMap.put(cellController.getCoord().toString(),cellController.copyPreviewStyle());
//
//        }

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


//        for (String coordStr : prevStyleMap.keySet()) {
//
//            CellController cellToChangeStyle = coordToCellControllerMap.get(coordStr);
//            cellToChangeStyle.setCellStyle(prevStyleMap.get(sheet.getCell(CoordinateImpl.parseCoordinate(coordStr)).getCoord().toString()));
//
//
//        }
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

        Boolean doubleClick = currentlyFocusedCoord != null && currentlyFocusedCoord.toString().equals(newCoord);
        if (doubleClick) {
            removeFocusingOfFocusedCell();
            mainController.handleCellClick(null);
            //mainController.updateActionLine(null);
            //mainController.setCellSelectedProperty(false);
        }
        else {
            clearMarkOfCells();
            currentlyFocusedCoord = CoordinateImpl.parseCoordinate(newCoord);
            currentlyFocusedCellController = coordToCellControllerMap.get(currentlyFocusedCoord.toString()); // עדכון התא הממוקד
            addFocusingToCell(currentlyFocusedCoord);
            mainController.handleCellClick(currentlyFocusedCoord);
            //mainController.updateActionLine(currentlyFocusedCoord);
            //mainController.setCellSelectedProperty(true);
        }

    }

    public void addFocusingToCell(Coordinate newFocusedCoord) {
        currentlyFocusedCellController = coordToCellControllerMap.get(newFocusedCoord.toString());
        currentlyFocusedCellController.setBorder("red", "3px");

        CellDTO cell = mainController.getCellDTO(newFocusedCoord.toString());
        List<Coordinate> dependsOn = cell.getDependsOn();
        for (Coordinate coord : dependsOn) {
            CellController depCellController = coordToCellControllerMap.get(coord.toString());
            depCellController.setBorder("blue", "2px");
        }

        List<Coordinate> influencingOn = cell.getInfluencingOn();
        for (Coordinate coord : influencingOn) {
            CellController infCellController = coordToCellControllerMap.get(coord.toString());
            infCellController.setBorder("green", "2px");
        }
    }

    public void removeFocusingOfFocusedCell() {
        //CellController currentlyFocusedCellController = coordToCellControllerMap.get(oldCellCoordinate);
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

            currentlyFocusedCoord = null;
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

//    public void setCellBorderColor(String colorStr) {
//        if (currentlyFocusedCellController != null) {
//            currentlyFocusedCellController.setBorderColor(colorStr);
//        }
//    }

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
        removeFocusingOfFocusedCell();
    }
}
