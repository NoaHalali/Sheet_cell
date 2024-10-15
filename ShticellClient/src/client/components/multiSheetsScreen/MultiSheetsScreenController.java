package client.components.multiSheetsScreen;

import client.components.mainAppController.AppController;
import client.components.multiSheetsScreen.commands.CommandsController;
import client.components.multiSheetsScreen.loadFiles.LoadSheetFilesController;
import client.components.multiSheetsScreen.permissionsTable.PermissionsTableController;
import client.components.multiSheetsScreen.sheetsTable.SheetsTableController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import shticell.permissions.PermissionType;
import shticell.permissions.RequestStatus;

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

    @FXML private ScrollPane permissionsTable;
    @FXML private PermissionsTableController permissionsTableController;

    private SimpleBooleanProperty sheetSelected;
    private SimpleBooleanProperty pendingRequestSelected ;

    private SimpleBooleanProperty hasOwnerPermission;
    private SimpleBooleanProperty hasWriterPermission;
    private SimpleBooleanProperty hasReaderPermission;

    private SimpleStringProperty selectedSheetName ;


    @FXML
    public void initialize()
    {
        //loadSheetFilesController.setParentController(this);
        sheetSelected= new SimpleBooleanProperty(false);
        pendingRequestSelected= new SimpleBooleanProperty(false);
        hasOwnerPermission= new SimpleBooleanProperty(false);
        hasWriterPermission= new SimpleBooleanProperty(false);
        hasReaderPermission= new SimpleBooleanProperty(false);

        selectedSheetName= new SimpleStringProperty("");

        loadSheetFilesController.setParentController(this);
        sheetsTableController.setParentController(this);
        commandsController.setParentController(this);
        permissionsTableController.setParentController(this);

        commandsController.initializeCommandsController(sheetSelected,pendingRequestSelected, selectedSheetName,
                hasOwnerPermission, hasWriterPermission, hasReaderPermission);

       // permissionsTableController.initializePermissionsTableController(sheetSelected);
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
        //String selectedSheetName = sheetsTableController.getSelectedSheetName();
        mainController.switchToSheetManager(selectedSheetName.get());
    }

    public void setActive() {
        //sheetSelected.set(false);
        //resetPermissionsProperties();
        selectedSheetName.set("");
        sheetsTableController.startListRefresher();
        System.out.println("MultiSheetsScreenController is active");
    }

    public void handleSheetSelect(String sheetSelectedName, PermissionType permissionForSelectedSheet) {

        sheetSelected.set(true);
        selectedSheetName.set(sheetSelectedName);
        updatePermissionsProperties(permissionForSelectedSheet);

        //commandsController.setSheetNameLabel(sheetSelectedName);
        permissionsTableController.startListRefresher();
    }

    public void handleRequestSelect(RequestStatus status){
        if( status == RequestStatus.PENDING){
            pendingRequestSelected.set(true);
        }else{
            pendingRequestSelected.set(false);
        }
    }

    public String getSelectedSheetName() {
        return selectedSheetName.get();
    }

    public int getSelectedRequestNumber() {
        return permissionsTableController.getSelectedRequestNumber();
    }

    private void updatePermissionsProperties(PermissionType permissionForSelectedSheet) {
        resetPermissionsProperties();
        switch (permissionForSelectedSheet){
            case OWNER:
                hasOwnerPermission.set(true);
                break;
            case WRITER:
                hasWriterPermission.set(true);
                break;
            case READER:
                hasReaderPermission.set(true);
                break;
        }
    }

    private void resetPermissionsProperties() {
        hasOwnerPermission.set(false);
        hasWriterPermission.set(false);
        hasReaderPermission.set(false);
    }

//    public void handlePermissionRequest(PermissionType permissionType) {
//        String selectedSheetName = sheetsTableController.getSelectedSheetName();
//        mainController.handlePermissionRequest(selectedSheetName, permissionType);
//    }
}
