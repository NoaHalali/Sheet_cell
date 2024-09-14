package components.left.ranges;

import components.MainComponent.AppController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
    //private String lastSelectedRange;

    public void initializeRangesController(List<String> existingRanges, SimpleBooleanProperty isCellSelected) {
        // אתחול הרשימה עם "None" בהתחלה
        refreshListView(existingRanges);

        // איפוס הבחירה ברשימה כאשר מתבצע אתחול
        rangeListView.getSelectionModel().clearSelection();

        // מאזין לבחירת פריט ברשימה
        rangeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // אם נבחר "None", איפוס הבחירה האחרונה
                if ("None".equals(newValue)) {
                    clearCurrSelectedRangeHighlight();
                } else {
                    viewRangeAction();
                }
            }
        });

        // מאזין למאפיין של בחירת תא מ-AppController
        isCellSelected.addListener((observable, oldValue, newValue) -> {
            // אם תא נבחר, איפוס הבחירה ברשימה
            if (newValue) {
                rangeListView.getSelectionModel().clearSelection();
            }
        });
    }



    private void refreshListView(List<String> ranges) {
        // יצירת רשימה חדשה בכל פעם שמאתחלים את הרשימה
        ObservableList<String> listViewItems = FXCollections.observableArrayList();
        listViewItems.add("None");

        // הוספת הרשימה המעודכנת מהלוגיקה
        if (ranges != null && !ranges.isEmpty()) {
            listViewItems.addAll(ranges);
        }

        rangeListView.setItems(listViewItems);

        // איפוס הבחירה ב-ListView
        rangeListView.getSelectionModel().clearSelection();
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
            //lastSelectedRange =null;
            // שליחת הפעולה ללוגיקה
            mainController.deleteRange(selectedRangeName);

            // רענון הרשימה ב-ListView
            refreshListView(mainController.getRanges());

        } catch (Exception e) {
            mainController.showAlert("Error", e.getMessage());
        }
    }

    @FXML
    public void viewRangeAction() {
        String selectedRangeName = rangeListView.getSelectionModel().getSelectedItem();
        if (selectedRangeName != null) {
            highlightRange(selectedRangeName);
        } else {
            mainController.showAlert("Error", "No range selected or range does not exist!");
        }
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }

    private void highlightRange(String rangeName) {
        mainController.highlightRange(rangeName);
    }

    private void clearCurrSelectedRangeHighlight() {
        mainController.clearCurrSelectedRangeHighlight(); // פונקציה להסרת ההדגשה ב-AppController
    }
}
