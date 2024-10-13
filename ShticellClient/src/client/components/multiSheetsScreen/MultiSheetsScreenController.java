package client.components.multiSheetsScreen;

import client.components.mainAppController.AppController;
import client.components.multiSheetsScreen.commands.CommandsController;
import client.components.multiSheetsScreen.loadFiles.LoadSheetFilesController;
import client.components.multiSheetsScreen.sheetsTable.SheetsTableController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MultiSheetsScreenController {
    private Stage primaryStage;
    private AppController mainController;
    @FXML private HBox loadSheetFiles;
    @FXML private LoadSheetFilesController loadSheetFilesController;

    @FXML private ScrollPane sheetsTable;
    @FXML private SheetsTableController sheetsTableController;

    @FXML private VBox commands;
    @FXML private CommandsController commandsController;

    private SimpleBooleanProperty sheetSelected ;


    @FXML
    public void initialize() {

        //loadSheetFilesController.setParentController(this);
        sheetSelected=new SimpleBooleanProperty(false);
        loadSheetFilesController.setParentController(this);
        sheetsTableController.setParentController(this);
        commandsController.setParentController(this);
        commandsController.initializeCommandsController(sheetSelected);

    }

    public File showFileChooser(FileChooser fileChooser) {
        return fileChooser.showOpenDialog(primaryStage);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public void switchToSheetManager() {
        String selectedSheetName = sheetsTableController.getSelectedSheetName();
        mainController.switchToSheetManager(selectedSheetName);
    }

    public void setActive() {
        commandsController.setSheetNameLabel("");
        sheetSelected.set(false);
        sheetsTableController.startListRefresher();
        System.out.println("MultiSheetsScreenController is active");

    }
    public void handleSheetSelected(String sheetSelectedName){
        sheetSelected.set(true);
        commandsController.setSheetNameLabel(sheetSelectedName);


    }
}
