package components.left.ranges;

import components.MainComponent.AppController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.List;

public class RangesController {

    @FXML private ComboBox<String> rangeComboBox;
    @FXML private TextField rangeNameField;
    @FXML private TextField rangeDefinitionField;
    @FXML private Button addRangeButton;
    @FXML private Button deleteRangeButton;

    private AppController mainController;

    public void initializeRangesController(List<String> existingRanges, SimpleBooleanProperty isCellSelected) {
        refreshComboBox(existingRanges);

        // Listener לבחירת טווח
        rangeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if ("None".equals(newValue)) {
                    clearCurrSelectedRangeHighlight();
                } else {
                    viewRangeAction();
                }
            }
        });

        // Listener לאיפוס הבחירה כאשר תא נבחר
        isCellSelected.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                rangeComboBox.getSelectionModel().clearSelection(); // איפוס הבחירה
                rangeComboBox.setValue(null); // הגדרת ערך נבחר ל-null כדי לאפס את התצוגה
            }
        });
    }

    private void refreshComboBox(List<String> ranges) {
        // יצירת רשימה חדשה בכל פעם שמאתחלים את הרשימה
        ObservableList<String> comboBoxItems = FXCollections.observableArrayList();
        comboBoxItems.add("None");

        if (ranges != null && !ranges.isEmpty()) {
            comboBoxItems.addAll(ranges);
        }

        rangeComboBox.setItems(comboBoxItems);

        // איפוס הבחירה ב-ComboBox
        rangeComboBox.getSelectionModel().clearSelection();
        rangeComboBox.setValue(null); // הגדרת ערך נבחר ל-null כדי לאפס את התצוגה
    }

    @FXML
    public void addRangeButtonAction() {
        String rangeName = rangeNameField.getText();
        String rangeDefinition = rangeDefinitionField.getText();

        try {
            mainController.addRange(rangeName, rangeDefinition);
            refreshComboBox(mainController.getRanges());

        } catch (Exception e) {
            mainController.showAlert("Error", e.getMessage());
        }
    }

    @FXML
    public void deleteRangeAction() {
        String selectedRangeName = rangeComboBox.getSelectionModel().getSelectedItem();
        if ("None".equals(selectedRangeName)) {
            mainController.showAlert("Error", "Cannot delete 'None'!");
            return;
        }
        if(selectedRangeName == null) {
            mainController.showAlert("Error", "No range selected!");
            return;
        }

        try {
            clearCurrSelectedRangeHighlight();
            mainController.deleteRange(selectedRangeName);
            refreshComboBox(mainController.getRanges());

        } catch (Exception e) {
            mainController.showAlert("Error", e.getMessage());
        }
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    private void clearCurrSelectedRangeHighlight() {
        mainController.clearCurrSelectedRangeHighlight();
    }

    private void viewRangeAction() {
        String selectedRangeName = rangeComboBox.getSelectionModel().getSelectedItem();
        if (selectedRangeName != null) {
            highlightRange(selectedRangeName);
        } else {
            mainController.showAlert("Error", "No range selected or range does not exist!");
        }
    }

    private void highlightRange(String rangeName) {
        mainController.highlightRange(rangeName);
    }
}
