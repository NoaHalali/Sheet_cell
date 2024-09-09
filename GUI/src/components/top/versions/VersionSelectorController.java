package components.top.versions;

import components.MainComponent.AppController;
import components.center.cellsTable.TableController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import parts.SheetDTO;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

public class VersionSelectorController {

    @FXML private ComboBox<String> versionSelector;
    private ObservableList<String> versionList = FXCollections.observableArrayList();
    private AppController mainController;

    public ComboBox<String> getVersionSelector() {
        return versionSelector;
    }

    // במקום initialize
    public void initializeVersionSelector(int numOfVersions) {

        // הגדרת רשימת הגרסאות ל-ComboBox
        versionSelector.setItems(versionList);
        versionSelector.setDisable(true); // הגדרת Disable דיפולטי
        setVersionSelectorOptions(numOfVersions);


        // הוספת מאזין לערך הנבחר ב-ComboBox
        versionSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleVersionSelection(newValue);
            }
        });
    }

    public void setVersionSelectorOptions(int numOfVersions) {
        // יצירת רשימה של מספרי גרסאות לפי מספר הגרסאות
        List<String> newList = IntStream.rangeClosed(1, numOfVersions)
                .mapToObj(version -> "Version " + version)
                .toList();

        versionList.setAll(newList);
        versionSelector.setDisable(false); // אפשר את ה-ComboBox לאחר טעינת הגרסאות
    }

    private void handleVersionSelection(String versionString) {
        //TODO - move to app controller
        // check if alerts are pop up, if it is so move it to the app controller
        // or maybe just bring it here?
        //mainController.handleVersionSelection(version);


        try {
            // Create a new stage for the popup
            String[] parts = versionString.split(" "); // מחלק את המחרוזת לפי רווח
            String versionNumberStr = parts[1]; // החלק השני של המערך הוא המספר

            Stage popupStage = new Stage();
            popupStage.setTitle("Version Preview: " + versionNumberStr);

            // Load the FXML or create a layout dynamically
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/center/cellsTable/table.fxml")); // Adjust the path
            Parent root = loader.load();

            // Get the TableController and initialize it with the selected version of the sheet
            TableController tableController = loader.getController();
            SheetDTO sheet = mainController.getSheetDTOByVersion(versionNumberStr);
            tableController.showSheetPreview(sheet);

            // Set the scene and show the popup
            Scene scene = new Scene(root);
            popupStage.setScene(scene);

            // Set the popup to block the main window (modality)
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Show the popup and wait for it to be closed before returning to the main window
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception if FXML loading fails
        }
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}



//public class VersionSelectorController {
//
//    @FXML private ComboBox<String> versionSelector; // הגדרה של ה-ComboBox
//    private ObservableList<String> versionList = FXCollections.observableArrayList(); // רשימת הגרסאות ב-ObservableList
//    private AppController mainController;
//
//    public ComboBox<String> getVersionSelector() {
//        return versionSelector;
//    }
//
//    @FXML
//    private void initialize() {
//        // הגדרת רשימת הגרסאות ל-ComboBox
//        versionSelector.setItems(versionList);
//
//        // הגדרת מצב דיפולטי של ComboBox ו-disable
//        initializeDefaultState();
//
//        // הוספת מאזין לערך הנבחר ב-ComboBox
//        versionSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                handleVersionSelection(newValue); // קריאה למתודה שמטפלת באירוע
//            }
//        });
//    }
//
//    // אתחול מצב דיפולטי של ה-ComboBox
//    public void initializeDefaultState() {
//        versionList.setAll("No versions available"); // ערך דיפולטי
//        versionSelector.setDisable(true); // הגדרת Disable ל-ComboBox
//    }
//
//    // פעולה להפיכת ה-ComboBox ל-enable
//    public void enableVersionSelection() {
//        versionSelector.setDisable(false); // הפיכת ה-ComboBox ל-enable
//    }
//
//    public void setVersions(List<Integer> versions) {
//        // המרה לרשימת מחרוזות עם הפורמט המתאים
//        List<String> newList = versions.stream()
//                .map(version -> "Version " + version)
//                .toList(); // עבור Java 16 ומעלה, או collect(Collectors.toList()) לגרסאות קודמות
//        versionList.setAll(newList); // עדכון הרשימה של ה-ComboBox
//    }
//
//    // פעולה לטיפול באירוע הבחירה בגרסה
//    private void handleVersionSelection(String version) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Version Information");
//        alert.setHeaderText("Selected Version: " + version);
//        alert.setContentText("Details about " + version + " will appear here.");
//        alert.showAndWait();
//    }
//
//    public void setMainController(AppController mainController) {
//        this.mainController = mainController;
//    }
//}
