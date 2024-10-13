package client.components.multiSheetsScreen.commands;

import client.components.multiSheetsScreen.MultiSheetsScreenController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class CommandsController {

    private MultiSheetsScreenController parentController;

    @FXML
    private Button handlePermissionRequest;

    @FXML
    private Button requestPermissionButton;

    @FXML
    private Button viewSheetButton;

    @FXML
    private Label sheetNameLabel;



    public void initializeCommandsController(SimpleBooleanProperty sheetSelected) {
       // sheetSelected = new SimpleBooleanProperty(false);
        viewSheetButton.disableProperty().bind(sheetSelected.not());
        requestPermissionButton.disableProperty().bind(sheetSelected.not());
    }


    public void handleViewSheetButtonClick(ActionEvent actionEvent) {
        parentController.switchToSheetManager();
    }

    public void setSheetNameLabel(String sheetName) {
        sheetNameLabel.setText(sheetName);
    }

    public void setParentController(MultiSheetsScreenController multiSheetsScreenController) {
        this.parentController = multiSheetsScreenController;
    }
}
