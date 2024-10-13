package client.components.multiSheetsScreen.commands;

import client.components.multiSheetsScreen.MultiSheetsScreenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class CommandsController {

    private MultiSheetsScreenController parentController;

    @FXML
    private Button handlePermissionRequest;

    @FXML
    private Button requestPermissionButton;

    @FXML
    private Button viewSheetButton;


    public void handleViewSheetButtonClick(ActionEvent actionEvent) {
        parentController.switchToSheetManager();
    }

    public void setParentController(MultiSheetsScreenController multiSheetsScreenController) {
        this.parentController = multiSheetsScreenController;
    }
}
