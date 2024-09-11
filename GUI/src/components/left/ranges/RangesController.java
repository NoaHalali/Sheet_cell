package components.left.ranges;

import components.MainComponent.AppController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML private ListView<String> rangeListView;

    private Map<String, String> ranges = new HashMap<>();
    private ObservableList<String> listViewItems; // הרשימה הנתמכת לצורך עדכון דינמי
    private AppController mainController;
    private String lastSelectedRange;

    public void initializeRangesController() {
        lastSelectedRange = null;

        // אתחול הרשימה עם "None" בהתחלה
        listViewItems = FXCollections.observableArrayList();
        listViewItems.add("None");
        rangeListView.setItems(listViewItems);

        rangeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // נקה את הסימון של הטווח הקודם אם קיים
                if (lastSelectedRange != null && ranges.containsKey(lastSelectedRange)) {
                    clearRangeHighlight(); // נקה את כל הסימונים
                }

                // אם נבחר "None", איפוס הבחירה האחרונה
                if ("None".equals(newValue)) {
                    lastSelectedRange = null;
                } else if (ranges.containsKey(newValue)) {
                    // אם הטווח קיים במפה, עדכן את הבחירה והצג את הטווח החדש
                    lastSelectedRange = newValue;
                    viewRangeAction(); // הצג את הטווח החדש
                } else {
                    // במידה ולא קיים, יש להציג הודעה או לטפל במקרה בהתאם
                    System.out.println("Selected range " + newValue + " does not exist.");
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
            listViewItems.add(rangeName); // הוספה לרשימת ה-ListView
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    @FXML
    public void deleteRangeAction() {
        String selectedRangeName = rangeListView.getSelectionModel().getSelectedItem();
        if ("None".equals(selectedRangeName)) {
            showAlert("Error", "Cannot delete 'None'!");
            return; // אין טווח נבחר או שלא ניתן למחוק את "None"
        }
        if(selectedRangeName == null) { //לא בטוח משתמשים אבל שיהיה בינתיים
            showAlert("Error", "No range selected!");
            return;
        }

        try {
            clearRangeHighlight();
            mainController.deleteRange(selectedRangeName);
            ranges.remove(selectedRangeName);

            // בדוק אם הטווח נמצא ברשימה לפני שמנסים להסירו
            if (listViewItems.contains(selectedRangeName)) {
                listViewItems.remove(selectedRangeName);
            }

            //lastSelectedRange = null; //

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
