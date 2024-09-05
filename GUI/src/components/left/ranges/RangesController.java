package components.left.ranges;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.util.HashMap;
import java.util.Map;

public class RangesController {

    @FXML private TextField rangeNameField;
    @FXML private TextField rangeDefinitionField;
    @FXML private Button addRangeButton;
    @FXML private Button deleteRangeButton;
    @FXML private Button viewRangeButton;
    @FXML private ListView<String> rangeListView;

    // מפה לשמירת הטווחים
    private Map<String, String> ranges = new HashMap<>();

    @FXML
    public void addRangeAction() {
        String rangeName = rangeNameField.getText();
        String rangeDefinition = rangeDefinitionField.getText();

        // בדיקה אם השם ייחודי
        if (ranges.containsKey(rangeName)) {
            showAlert("Error", "Range name already exists!");
        } else {
            ranges.put(rangeName, rangeDefinition);
            rangeListView.getItems().add(rangeName);
        }
    }

    @FXML
    public void deleteRangeAction() {
        String selectedRange = rangeListView.getSelectionModel().getSelectedItem();
        if (selectedRange != null && ranges.containsKey(selectedRange)) {
            // לוגיקה למניעת מחיקה אם יש שימוש ב-range
            ranges.remove(selectedRange);
            rangeListView.getItems().remove(selectedRange);
        } else {
            showAlert("Error", "No range selected or range does not exist!");
        }
    }

    @FXML
    public void viewRangeAction() {
        String selectedRange = rangeListView.getSelectionModel().getSelectedItem();
        if (selectedRange != null && ranges.containsKey(selectedRange)) {
            String rangeDefinition = ranges.get(selectedRange);
            highlightRange(rangeDefinition); // פונקציה להדגשת הטווח בתצוגה
        } else {
            showAlert("Error", "No range selected or range does not exist!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void highlightRange(String rangeDefinition) {
        // לוגיקה להדגשת הטווח בתצוגה
    }
}
