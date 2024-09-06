package components.center.cellsTable;

import components.MainComponent.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import parts.CellDTO;
import parts.SheetDTO;
import parts.cell.coordinate.Coordinate;
import parts.cell.coordinate.CoordinateImpl;
import parts.cell.expression.effectiveValue.CellType;
import parts.cell.expression.effectiveValue.EffectiveValue;

import java.util.HashMap;
import java.util.Map;

public class TableController {

    private AppController mainController;
    @FXML private GridPane dynamicGridPane;
    private Map<String, Label> cellMap=new HashMap<>();


    public void initializeGrid(SheetDTO sheet) {

        int rows = sheet.getNumberOfRows();
        int cols = sheet.getNumberOfCols();

        CoordinateImpl coord ;
        // Clear existing constraints and children
        dynamicGridPane.getColumnConstraints().clear();
        dynamicGridPane.getRowConstraints().clear();
        dynamicGridPane.getChildren().clear();

        // Set column constraints
        for (int col = 0; col <=cols; col++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPrefWidth(100);  // Set preferred width for each column
            dynamicGridPane.getColumnConstraints().add(colConst);
        }

        // Set row constraints
        for (int row = 0; row <=rows; row++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPrefHeight(30);  // Set preferred height for each row
            dynamicGridPane.getRowConstraints().add(rowConst);
        }

        // Populate the grid with empty labels
        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= cols; col++) {
                coord = new CoordinateImpl(row, col);
                CellDTO cell = sheet.getCell(coord);
                Label label = new Label(cell == null ? "": calcValueToPrint(cell.getEffectiveValue()));  // Start with empty labels
                label.setStyle("-fx-background-color: #f0f0f0; -fx-alignment: center; -fx-border-color: blue; -fx-border-width: 0.5px;");
                dynamicGridPane.add(label, col, row);
                label.setMaxWidth(Double.MAX_VALUE);
                label.setMaxHeight(Double.MAX_VALUE);

                String coordStr = coord.toString();
                label.setOnMouseClicked(event -> {
                    label.setStyle("-fx-background-color: #f0f0f0; -fx-alignment: center; -fx-border-color: red; -fx-border-width: 0.5px;");

                    mainController.updateActionLine(coordStr);

                // Call the method in the main controller with the key
                });

                cellMap.put(coordStr, label);
            }
        }
        for (int row = 1; row <=rows; row++) {

            String labelText = String.valueOf((char) ('1' + row-1));
            Label label = new Label(labelText);  // Create label with the letter
            label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);  // Ensure the label fills the cell
            label.setStyle("-fx-background-color: #f0f0f0; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 0.5px;");
            label.setMaxWidth(Double.MAX_VALUE);
            label.setMaxHeight(Double.MAX_VALUE);
            dynamicGridPane.add(label, 0, row);  // Add label to the first column
        }
        for (int col = 1; col <=cols; col++) {

            String labelText = String.valueOf((char) ('A' + col-1));
            Label label = new Label(labelText);  // Create label with the letter
            label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);  // Ensure the label fills the cell
            label.setStyle("-fx-background-color: #f0f0f0; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 0.5px;");
            label.setMaxWidth(Double.MAX_VALUE);
            label.setMaxHeight(Double.MAX_VALUE);
            dynamicGridPane.add(label, col, 0);
        }
       // updateCellContent(2,2,"noa");
        paintCell(2,2,"green");
    }
    public void paintCell(int row, int col, String color) {
        // Retrieve the label from the cellMap using the row and column indices
        Label label = cellMap.get(row + ":" + col);

        // If the label exists, change its background color
        if (label != null) {
            label.setStyle("-fx-background-color: " + color + "; -fx-border-color: black; -fx-alignment: center;");
        } else {
            System.out.println("Cell at row " + row + " and column " + col + " does not exist.");
        }
    }
//    public void updateData(){
//        mainController.updateHbox()
//    }

    public void updateCellContent(Coordinate coord , EffectiveValue newText) {
      //  String key = row + ":" + col;
        Label label = cellMap.get(coord.toString());
        if (label != null) {
            label.setText(calcValueToPrint(newText));
        }
    }
    public String calcValueToPrint(EffectiveValue effectiveValue) {
        if(effectiveValue == null) {
            return "";
        }
        if (effectiveValue.getCellType() == CellType.NUMERIC) {
            double num = effectiveValue.extractValueWithExpectation(Double.class);

            if (Double.isNaN(num) || Double.isInfinite(num)) {
                return "NaN"; // החזר "NaN" אם הערך הוא NaN או Infinity
            }

            if (num == Math.floor(num)) {
                // אם המספר הוא שלם, הדפס אותו ללא נקודות עשרוניות
                return String.format("%,d", (long) num);
            } else {
                // אם יש נקודה עשרונית, הדפס עם שתי ספרות אחרי הנקודה
                return String.format("%,.2f", num);
            }
        } else if (effectiveValue.getCellType() == CellType.STRING) {
            return effectiveValue.extractValueWithExpectation(String.class).trim();
        } else if (effectiveValue.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(effectiveValue.extractValueWithExpectation(Boolean.class)).toUpperCase();
        } else {
            throw new IllegalArgumentException(); // TODO - פתרון זמני
        }
    }
    public void RemoveFocusingOfCell(String cellCoordinate) {
        cellMap.get(cellCoordinate).setStyle( "-fx-background-color: #f0f0f0; -fx-alignment: center; -fx-border-color: blue; -fx-border-width: 0.5px;");
    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}

