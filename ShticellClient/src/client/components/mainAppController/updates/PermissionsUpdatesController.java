package client.components.mainAppController.updates;

import client.components.multiSheetsScreen.MultiSheetsScreenController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;

import static client.components.Utils.Constants.REFRESH_RATE;

public class PermissionsUpdatesController {

    @FXML
    private Label messageLabel;


    private String selectedSheetName;
    private MultiSheetsScreenController parentController;
    //private PermissionType permissionForSelectedSheet;

    // @FXML private TableColumn<SheetDetailsDTO, String> permissionColumn;

    private Timer timer;
    private TimerTask messageRefresher;



    @FXML
    public void initialize() {

    }

    private void updateMessage(String message) {
        Platform.runLater(() -> {
        messageLabel.setText(message);
        });
    }

    public void startMessageRefresher() {

        messageRefresher = new PermissionsUpdatesRefresher(this::updateMessage);
        timer = new Timer();
        timer.schedule(messageRefresher, REFRESH_RATE, 5000);
    }

    public String getSelectedSheetName() {
        return selectedSheetName;
    }

    public void setParentController(MultiSheetsScreenController parentController) {
        this.parentController = parentController;
    }
}
