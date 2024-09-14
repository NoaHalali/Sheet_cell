package components.left.ranges;

import components.MainComponent.AppController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.util.List;

public class RangesController {

    @FXML private ListView<String> rangeListView;
    @FXML private TextField rangeNameField;
    @FXML private TextField rangeDefinitionField;
    @FXML private Button addRangeButton;
    @FXML private Button deleteRangeButton;

    private AppController mainController;

    public void initializeRangesController(List<String> existingRanges, SimpleBooleanProperty isCellSelected) {
        refreshListView(existingRanges);

        // Listener לבחירת טווח
        rangeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
                clearSelection(); // קריאה למתודת איפוס הבחירה
            }
        });
    }

    private void refreshListView(List<String> ranges) {
        // יצירת רשימה חדשה בכל פעם שמאתחלים את הרשימה
        ObservableList<String> listViewItems = FXCollections.observableArrayList();
        listViewItems.add("None");

        if (ranges != null && !ranges.isEmpty()) {
            listViewItems.addAll(ranges);
        }

        rangeListView.setItems(listViewItems);

        clearSelection(); // איפוס הבחירה לאחר עדכון הפריטים
    }

    private void clearSelection() {
        rangeListView.getSelectionModel().clearSelection(); // איפוס הבחירה ב-ListView
    }

    @FXML
    public void addRangeButtonAction() {
        String rangeName = rangeNameField.getText();
        String rangeDefinition = rangeDefinitionField.getText();

        try {
            mainController.addRange(rangeName, rangeDefinition);
            refreshListView(mainController.getRanges());

        } catch (Exception e) {
            mainController.showAlert("Error", e.getMessage());
        }
    }

    @FXML
    public void deleteRangeAction() {
        String selectedRangeName = rangeListView.getSelectionModel().getSelectedItem();
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
            refreshListView(mainController.getRanges());

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
        String selectedRangeName = rangeListView.getSelectionModel().getSelectedItem();
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
