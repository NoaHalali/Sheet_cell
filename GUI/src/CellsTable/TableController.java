package CellsTable;

import MainComponent.AppController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
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
        for (int col = 0; col < cols; col++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPrefWidth(100);  // Set preferred width for each column
            dynamicGridPane.getColumnConstraints().add(colConst);
        }

        // Set row constraints
        for (int row = 0; row < rows; row++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPrefHeight(30);  // Set preferred height for each row
            dynamicGridPane.getRowConstraints().add(rowConst);
        }
//        for (int row = 0; row < rows; row++) {
//            Label label = new Label(String.valueOf(row));
//            dynamicGridPane.add(label, 0, row);
//        }
              //java fx show grid line
        // Populate the grid with labels
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Label label = new Label("Row " + row + " Col " + col);
                label.setStyle("-fx-background-color: white; -fx-alignment: center;");
                dynamicGridPane.add(label, col, row);
                cellMap.put(row + ":" + col, label);
            }
        }
        updateCellContent(2,2,"noa");
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

