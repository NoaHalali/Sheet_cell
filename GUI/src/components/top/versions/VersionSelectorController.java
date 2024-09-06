package components.top.versions;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class VersionSelectorController {
    @FXML
    private ComboBox<String> versionSelector; // הגדרה של ה-ComboBox

    public ComboBox<String> getVersionSelector() {
        return versionSelector;
    }
}
