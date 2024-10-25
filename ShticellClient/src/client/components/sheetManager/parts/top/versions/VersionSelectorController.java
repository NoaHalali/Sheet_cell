package client.components.sheetManager.parts.top.versions;

import client.components.Utils.StageUtils;
import client.components.sheetManager.parts.center.cellsTable.TableController;
import client.components.sheetManager.SheetManagerController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import static client.components.Utils.Constants.TABLE_FXML_RESOURCE_LOCATION;

public class VersionSelectorController {

    @FXML private MenuButton versionMenuButton;
    private SheetManagerController parentController;

    public void setVersionSelectorOptions(int numOfVersions) {
        versionMenuButton.getItems().clear();
        // יצירת רשימה של מספרי גרסאות לפי מספר הגרסאות
        List<String> newList = IntStream.rangeClosed(1, numOfVersions)
                .mapToObj(version -> "Version " + version)
                .toList();

        // הוספת פריטים חדשים ל-MenuButton
        for (String version : newList) {
            MenuItem item = new MenuItem(version);
            item.setOnAction(event -> handleVersionSelection(version));
            versionMenuButton.getItems().add(item);
        }

        versionMenuButton.setDisable(false); // הפיכת MenuButton לזמין לאחר טעינת הגרסאות
    }


    private void handleVersionSelection(String versionString) {
        try {
            // יצירת Stage חדש עבור החלון הקופץ
            String[] parts = versionString.split(" "); // מחלק את המחרוזת לפי רווח
            String versionNumberStr = parts[1]; // החלק השני של המערך הוא המספר

            Stage popupStage = new Stage();
            popupStage.setTitle("Version Preview: " + versionNumberStr);

            // טעינת FXML או יצירת ממשק באופן דינמי
            FXMLLoader loader = new FXMLLoader(getClass().getResource(TABLE_FXML_RESOURCE_LOCATION)); // Adjust the path
            Parent root = loader.load();

            // קבלת ה-TableController ואתחולו עם הגרסה הנבחרת של הגיליון
            TableController tableController = loader.getController();
             parentController.getSheetDTOByVersion(versionNumberStr, sheet->{
               tableController.showSheetPreview(sheet);
                     Scene scene = new Scene(root);
                     popupStage.setScene(scene);
                     popupStage.initModality(Modality.APPLICATION_MODAL);
                     popupStage.showAndWait();
        });

        } catch (IOException e) {
            StageUtils.showAlert("Error", e.getMessage());
        }
    }

    public void setParentController(SheetManagerController mainController) {
        this.parentController = mainController;
    }
}
