package client.components.multiSheetsScreen;

import client.components.mainAppController.AppController;
import client.components.multiSheetsScreen.loadFiles.LoadSheetFilesController;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MultiSheetsScreenController {
    private Stage primaryStage;
    private AppController mainController;
    @FXML  private HBox loadSheetFiles;
    @FXML LoadSheetFilesController loadSheetFilesController;

    @FXML
    public void initialize() {

        //loadSheetFilesController.setParentController(this);

      loadSheetFilesController.setParentController(this);
    }

    public File showFileChooser(FileChooser fileChooser) {
        return fileChooser.showOpenDialog(primaryStage);
    }
    public void setPrimaryStage(Stage primaryStage) {    this.primaryStage = primaryStage;}


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void switchToSheetManager() {
        mainController.switchToSheetManager();
    }

//    public void switchToSheetManager() {
//        mainController.switchToSheetManager();
//    }
}
