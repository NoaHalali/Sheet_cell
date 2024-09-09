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
import parts.cell.coordinate.Coordinate;
import parts.cell.coordinate.CoordinateImpl;
import parts.cell.expression.effectiveValue.CellType;
import parts.cell.expression.effectiveValue.EffectiveValue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableController {

    private AppController mainController;
    @FXML private GridPane dynamicGridPane;
    private Map<String, CellController> coordToCellControllerMap = new HashMap<>();
    private Coordinate currentlyFocusedCoord; // משתנה לשמירת המיקום של התא הממוקד כרגע

    public void initializeGrid(SheetDTO sheet) {

        int rows = sheet.getNumberOfRows();
        int cols = sheet.getNumberOfCols();

        CoordinateImpl coord;
        dynamicGridPane.getColumnConstraints().clear();
        dynamicGridPane.getRowConstraints().clear();
        dynamicGridPane.getChildren().clear();

        for (int col = 0; col <= cols; col++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPrefWidth(sheet.getColumnWidth());
            dynamicGridPane.getColumnConstraints().add(colConst);
        }

        for (int row = 0; row <= rows; row++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPrefHeight(sheet.getRowHeight());
            dynamicGridPane.getRowConstraints().add(rowConst);
        }

        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= cols; col++) {
                coord = new CoordinateImpl(row, col);
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/center/cell/cell.fxml"));
                    BorderPane cellPane = loader.load();
                    CellController cellController = loader.getController();

                    CellDTO cell = sheet.getCell(coord);
                    String cellText = cell == null ? "" : calcValueToPrint(cell.getEffectiveValue());
                    cellController.setText(cellText);
                    cellController.setBackgroundColor("#f0f0f0");
                    cellController.setAlignment("center");
                    cellController.setBorderColor("black");
                    cellController.setBorderWidth("0.5px");

                    String coordStr = coord.toString();
                    cellPane.setOnMouseClicked(event -> {
                        handleCellClick(coordStr); // קריאה למתודה שמטפלת בלחיצה על תא
                    });

                    dynamicGridPane.add(cellPane, col, row);
                    coordToCellControllerMap.put(coordStr, cellController);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        addRowAndColumnLabels(rows, cols);

//        currentlyFocusedCoord.valueProperty().addListener((observable, oldValue, newValue) -> {
//            counterLabel.setText("Count: " + newValue.intValue());
//        });

    }

    private void addRowAndColumnLabels(int rows, int cols) {
        for (int row = 1; row <= rows; row++) {
            String labelText = String.valueOf((char) ('1' + row - 1));
            Label label = new Label(labelText);
            label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            label.setStyle("-fx-background-color: #f0f0f0; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 0.5px;");
            dynamicGridPane.add(label, 0, row);
        }
        for (int col = 1; col <= cols; col++) {
            String labelText = String.valueOf((char) ('A' + col - 1));
            Label label = new Label(labelText);
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

    public String calcValueToPrint(EffectiveValue effectiveValue) {
        if (effectiveValue == null) {
            return "";
        }
        if (effectiveValue.getCellType() == CellType.NUMERIC) {
            double num = effectiveValue.extractValueWithExpectation(Double.class);

            if (Double.isNaN(num) || Double.isInfinite(num)) {
                return "NaN";
            }

            if (num == Math.floor(num)) {
                return String.format("%,d", (long) num);
            } else {
                return String.format("%,.2f", num);
            }
        } else if (effectiveValue.getCellType() == CellType.STRING) {
            return effectiveValue.extractValueWithExpectation(String.class).trim();
        } else if (effectiveValue.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(effectiveValue.extractValueWithExpectation(Boolean.class)).toUpperCase();
        } else {
            throw new IllegalArgumentException();
        }
    }



    public void paintCellsInfluencingAndDependsOnBorders() {
        // Implement this method if needed
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    private void handleCellClick(String newCoord) {
        //CellDTO newCell = mainController.getCellDTO(newCoord);

        if (currentlyFocusedCoord != null && currentlyFocusedCoord.toString().equals(newCoord)) {
            // אם זהו התא הממוקד כרגע, הסר את המיקוד ועדכן את השורה לריקה

            removeFocusingOfCell(currentlyFocusedCoord.toString());
            currentlyFocusedCoord = null;
            mainController.updateActionLine(null); // עדכן את ה-action line
        } else {
            // אם זה תא חדש, הסר מיקוד מהתא הקודם
            if (currentlyFocusedCoord != null) {
                removeFocusingOfCell(currentlyFocusedCoord.toString());
            }
            // עדכן תא ממוקד חדש
            currentlyFocusedCoord = CoordinateImpl.parseCoordinate(newCoord);
            addFocusingToCell(currentlyFocusedCoord);
            mainController.updateActionLine(currentlyFocusedCoord); // עדכן את ה-action line
        }
    }

    private void addFocusingToCell(Coordinate newFocusedCoord) {
        CellController cellController = coordToCellControllerMap.get(newFocusedCoord.toString());
        cellController.setBorderColor("red");
        cellController.setBorderWidth("3px");

        // Adding dependencies colors
        CellDTO cell = mainController.getCellDTO(newFocusedCoord.toString());
        List<Coordinate> dependsOn = cell.getDependsOn();
        for (Coordinate coord : dependsOn) {
            CellController depCellController = coordToCellControllerMap.get(coord.toString());
            depCellController.setBorderColor("blue");
            depCellController.setBorderWidth("2px");  // עדכון של התא המושפע בלבד
        }

        List<Coordinate> influencingOn = cell.getInfluencingOn();
        for (Coordinate coord : influencingOn) {
            CellController infCellController = coordToCellControllerMap.get(coord.toString());
            infCellController.setBorderColor("green");
            infCellController.setBorderWidth("2px");  // עדכון של התא המשפיע בלבד
        }
    }


    public void removeFocusingOfCell(String oldCellCoordinate) {
        CellController cellController = coordToCellControllerMap.get(oldCellCoordinate);
        if (cellController != null) {
            cellController.setBorderColor("black");
            cellController.setBorderWidth("0.5px");

            // Remove dependencies color
            CellDTO cell = mainController.getCellDTO(oldCellCoordinate);
            List<Coordinate> dependsOn = cell.getDependsOn();
            for (Coordinate coord : dependsOn) {
                CellController depCellController = coordToCellControllerMap.get(coord.toString());
                depCellController.setBorderColor("black");
                depCellController.setBorderWidth("0.5px");
            }
            List<Coordinate> influencingOn = cell.getInfluencingOn();
            for (Coordinate coord : influencingOn) {
                CellController infCellController = coordToCellControllerMap.get(coord.toString());
                infCellController.setBorderColor("black");
                infCellController.setBorderWidth("0.5px");
            }
        }
    }

    public Coordinate getCurrentlyFocusedCoord() {
        return currentlyFocusedCoord;
    }
}
