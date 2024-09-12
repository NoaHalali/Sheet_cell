package components.left.ranges;

import components.MainComponent.AppController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class RangesController {

    @FXML private TextField rangeNameField;
    @FXML private TextField rangeDefinitionField;
    @FXML private Button addRangeButton;
    @FXML private Button deleteRangeButton;
    @FXML private ListView<String> rangeListView;

    private AppController mainController;
    private String lastSelectedRange;

    public void initializeRangesController(List<String> existingRanges) {
        lastSelectedRange = null;

        // אתחול הרשימה עם "None" בהתחלה
        refreshListView(existingRanges);

        rangeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // נקה את הסימון של הטווח הקודם אם קיים
                if (lastSelectedRange != null) {
                    clearRangeHighlight(); // נקה את כל הסימונים
                }

                // אם נבחר "None", איפוס הבחירה האחרונה
                if ("None".equals(newValue)) {
                    lastSelectedRange = null;
                } else {
                    // אם הטווח קיים ברשימה, עדכן את הבחירה והצג את הטווח החדש
                    lastSelectedRange = newValue;
                    viewRangeAction(); // הצג את הטווח החדש
                }
            }
        });
    }

    private void refreshListView(List<String> ranges) {
        // אתחול הרשימה עם "None" בהתחלה
        ObservableList<String> listViewItems = FXCollections.observableArrayList();
        listViewItems.add("None");

        // הוספת הרשימה המעודכנת מהלוגיקה
        if (ranges != null && !ranges.isEmpty()) {
            listViewItems.addAll(ranges);
        }

        rangeListView.setItems(listViewItems);
    }

    @FXML
    public void addRangeButtonAction() {
        String rangeName = rangeNameField.getText();
        String rangeDefinition = rangeDefinitionField.getText();

        try {
            // שליחת הפעולה ללוגיקה
            mainController.addRange(rangeName, rangeDefinition);

            // רענון הרשימה ב-ListView
            refreshListView(mainController.getRanges());

        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    @FXML
    public void deleteRangeAction() {
        String selectedRangeName = rangeListView.getSelectionModel().getSelectedItem();
        if ("None".equals(selectedRangeName)) {
            showAlert("Error", "Cannot delete 'None'!");
            return;
        }
        if(selectedRangeName == null) {
            showAlert("Error", "No range selected!");
            return;
        }

        try {
            clearRangeHighlight();
            lastSelectedRange =null;
            // שליחת הפעולה ללוגיקה
            mainController.deleteRange(selectedRangeName);

            // רענון הרשימה ב-ListView
            refreshListView(mainController.getRanges());

        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    @FXML
    public void viewRangeAction() {
        String selectedRangeName = rangeListView.getSelectionModel().getSelectedItem();
        if (selectedRangeName != null) {
            highlightRange(selectedRangeName);
        } else {
            showAlert("Error", "No range selected or range does not exist!");
        }
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void highlightRange(String rangeName) {
        mainController.highlightRange(rangeName);
    }

    private void clearRangeHighlight() {
        mainController.clearRangeHighlight(lastSelectedRange); // פונקציה להסרת ההדגשה ב-AppController
    }
}
