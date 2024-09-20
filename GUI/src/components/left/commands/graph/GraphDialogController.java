package components.left.commands.graph;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class GraphDialogController {
    @FXML
    private ComboBox<String> xAxisComboBox;

    @FXML
    private ComboBox<String> yAxisComboBox;

    @FXML
    public void initialize() {
        // אתחול קומבובוקסים עם שמות העמודות האפשריות
        xAxisComboBox.setItems(FXCollections.observableArrayList(/* כאן השמות של העמודות */));
        yAxisComboBox.setItems(FXCollections.observableArrayList(/* כאן השמות של העמודות */));
    }

    @FXML
    private void handleCreateGraph() {
        String selectedX = xAxisComboBox.getValue();
        String selectedY = yAxisComboBox.getValue();

        // כאן נעביר את המידע לבניית הגרף בפועל
        if (selectedX != null && selectedY != null) {
            createGraph(selectedX, selectedY);
        } else {
            // טיפול במצב בו לא נבחרו עמודות
        }
    }

    private void createGraph(String xColumn, String yColumn) {
        // לוגיקה ליצירת הגרף כאן
    }
}

