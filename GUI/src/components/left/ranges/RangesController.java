package components.left.ranges;

import components.MainComponent.AppController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
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
    //@FXML private Button viewRangeButton;
    @FXML private ListView<String> rangeListView;

    // מפה לשמירת הטווחים
    private Map<String, String> ranges = new HashMap<>();
    private AppController mainController;
    private String lastSelectedRange; // משתנה לשמירת הבחירה האחרונה
    //private SimpleBooleanProperty isRangeSelected;


    public void initializeRangesController() {
        //isRangeSelected= new SimpleBooleanProperty(false);
        lastSelectedRange = null;
        //deleteRangeButton.disableProperty().bind(isRangeSelected.not());

        rangeListView.setItems(FXCollections.observableArrayList("None"));
        rangeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if(lastSelectedRange != null) {
                    clearRangeHighlight(); // נקה את כל הסימונים
                }
                // אם נבחר "None", נקה את הסימון
                if ("None".equals(newValue)) {
                    lastSelectedRange = null; // איפוס הבחירה האחרונה
                } else {
                    // במקרה שנבחר טווח אחר, נקה את הסימון והצג את הטווח החדש
                    //clearRangeHighlight();
                    lastSelectedRange = newValue; // עדכון הבחירה האחרונה
                    viewRangeAction(); // הצג את הטווח החדש
                }
            }
        });

    }

    @FXML
    public void addRangeButtonAction() {
        String rangeName = rangeNameField.getText();
        String rangeDefinition = rangeDefinitionField.getText();

        try {
            mainController.addRange(rangeName, rangeDefinition);
            ranges.put(rangeName, rangeDefinition);
            rangeListView.getItems().add(rangeName);
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    @FXML
    public void deleteRangeAction() {
        String selectedRangeName = rangeListView.getSelectionModel().getSelectedItem();
        try
        {
            mainController.deleteRange(selectedRangeName);
            ranges.remove(selectedRangeName);
            rangeListView.getItems().remove(selectedRangeName);
        }catch (Exception e) {
            showAlert("Error", e.getMessage());
        }


//        if (selectedRange != null && ranges.containsKey(selectedRange)) {
//            ranges.remove(selectedRange);
//            rangeListView.getItems().remove(selectedRange);
//        } else {
//            showAlert("Error", "No range selected or range does not exist!");
//        }
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
