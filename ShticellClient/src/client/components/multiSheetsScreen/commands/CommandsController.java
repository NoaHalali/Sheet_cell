package client.components.multiSheetsScreen.commands;

import client.components.multiSheetsScreen.MultiSheetsScreenController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;


public class CommandsController {

    private MultiSheetsScreenController parentController;

    @FXML
    private Button handlePermissionRequest;

    @FXML
    private MenuButton requestPermissionMenuButton;


    @FXML
    private Button viewSheetButton;

    @FXML
    private Label sheetNameLabel;



    public void initializeCommandsController(SimpleBooleanProperty sheetSelected) {
       // sheetSelected = new SimpleBooleanProperty(false);
        viewSheetButton.disableProperty().bind(sheetSelected.not());
        requestPermissionMenuButton.disableProperty().bind(sheetSelected.not());
        MenuButton menuButton = new MenuButton("Options");         // יצירת פריטי MenuItemMenuItem option1 = new MenuItem("Option 1");         MenuItem option2 = new MenuItem("Option 2");         MenuItem option3 = new MenuItem("Option 3");         // הוספת פריטי MenuItem ל-MenuButton        menuButton.getItems().addAll(option1, option2, option3);         // הגדרת אירועי לחיצה (onClick) עבור כל MenuItem        option1.setOnAction(event -> System.out.println("Option 1 clicked")); option2.setOnAction(event -> System.out.println("Option 2 clicked")); option3.setOnAction(event -> System.out.println("Option 3 clicked"));        // יצירת פריטי MenuItemMenuItem option1 = new MenuItem("Option 1");         MenuItem option2 = new MenuItem("Option 2");         MenuItem option3 = new MenuItem("Option 3");         // הוספת פריטי MenuItem ל-MenuButton        menuButton.getItems().addAll(option1, option2, option3);         // הגדרת אירועי לחיצה (onClick) עבור כל MenuItem        option1.setOnAction(event -> System.out.println("Option 1 clicked")); option2.setOnAction(event -> System.out.println("Option 2 clicked")); option3.setOnAction(event -> System.out.println("Option 3 clicked"));
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
