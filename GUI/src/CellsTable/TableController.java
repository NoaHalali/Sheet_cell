package CellsTable;

import MainComponent.AppController;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.HashMap;
import java.util.Map;

public class TableController {

    private AppController mainController;
    @FXML
    private GridPane dynamicGridPane;
    private Map<String, Label> cellMap=new HashMap<>();

    public void initializeGrid(int rows, int cols) {
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
                Label label = new Label("");  // Start with empty labels
                label.setStyle("-fx-background-color: #f0f0f0; -fx-alignment: center; -fx-border-color: blue; -fx-border-width: 0.5px;");
                dynamicGridPane.add(label, col, row);
                label.setMaxWidth(Double.MAX_VALUE);
                label.setMaxHeight(Double.MAX_VALUE);
                //label.setOnMouseClicked(E);נפעיל מתודה שתקרא לאבא וישלח את הקוארדינטת שורה
                cellMap.put(row + ":" + col, label);
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
        updateCellContent(2,2,"noa");
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

    public void updateCellContent(int row, int col, String newText) {
        String key = row + ":" + col;
        Label label = cellMap.get(key);
        if (label != null) {
            label.setText(newText);
        }
    }
    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}

