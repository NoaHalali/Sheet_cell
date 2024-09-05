package FileChooser;

import MainComponent.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;

public class FileChooserController {

    private AppController mainController;

    @FXML private Label filePathLabel;
    @FXML private Button loadFileButton;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML public void openFileButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select shticell file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = mainController.showFileSelector(fileChooser);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        filePathLabel.setText(absolutePath);

        // קריאה ל-loadFile
        try {
            mainController.loadFile(absolutePath);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
