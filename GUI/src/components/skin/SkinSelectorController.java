package components.skin;

import components.MainComponent.AppController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;

public class SkinSelectorController {

    private AppController mainController;
    private Scene scene;

    @FXML
    private ComboBox<String> skinComboBox;

    public void setScene(Scene scene) {
        this.scene = scene;  // קבלת הסצנה מהמנהל הראשי
    }

    // אתחול הבחירה ב-SkinSelector
    public void initializeSkinSelector() {
        skinComboBox.getItems().addAll("Default", "Theme 1", "Theme 2", "neoTech");
        skinComboBox.getSelectionModel().selectFirst();

        skinComboBox.setOnAction(event -> {
            String selectedSkin = skinComboBox.getSelectionModel().getSelectedItem().toString();
            changeSkin(selectedSkin);  // קריאה לפונקציה שמבצעת את שינוי ה-skin
        });
    }

    public void changeSkin(String selectedSkin) {
        if (scene == null) return;  // וידוא שהסצנה קיימת לפני שינוי ערכת הנושא
        switch (selectedSkin) {
            case "Default":
                applyDefaultSkin();
                break;
            case "Theme 1":
                applyTheme1();
                break;
            case "Theme 2":
                applyTheme2();
                break;
            case "neoTech":
                applyNeoTech();
                break;
        }
    }

    private void applyNeoTech() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("neoTech.css").toExternalForm());
    }

    public void applyDefaultSkin() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("default.css").toExternalForm());
    }

    public void applyTheme1() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("theme1.css").toExternalForm());
    }

    public void applyTheme2() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("theme2.css").toExternalForm());
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}