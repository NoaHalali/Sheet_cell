package client.components.sheetManager.parts.top.updates;

import client.components.multiSheetsScreen.MultiSheetsScreenController;
import client.components.sheetManager.SheetManagerController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;

import static client.components.Utils.Constants.REFRESH_RATE;

public class SheetUpdatesController {

    @FXML
    private Label messageLabel;
    private String lastMessage = ""; // משתנה לשמירת ההודעה האחרונה
    @FXML private Button refreshButton;
    private SheetManagerController parentController;
    //String selectedSheet;

    private Timer timer;
    private TimerTask messageRefresherTask;


    @FXML
    public void initialize() {
        messageLabel.setVisible(false);
        refreshButton.setVisible(false); // הכפתור מוסתר כברירת מחדל
    }

    public void startVersionUpdateMessageRefresher(String sheetName) {

        messageRefresherTask = new SheetUpdatesRefresher(this::updateMessage,sheetName );
        timer = new Timer();
        timer.schedule(messageRefresherTask, REFRESH_RATE, 5000);
    }


    public void updateMessage(String message) {
        Platform.runLater(() -> {
        if (!message.equals(lastMessage)) {
            show();
            //messageLabel.setVisible(true);
            messageLabel.setText(message);
            lastMessage = message;

            messageLabel.setStyle("-fx-background-color: #f2f273; -fx-text-fill: #000000;");

           // refreshButton.setVisible(true);
        }
    });
    }

    @FXML
    private void handleRefreshButtonClick() {

        hide();

        System.out.println("Refreshing the sheet...");
        System.out.println("Button visible: " + refreshButton.isVisible());
        System.out.println("Button managed: " + refreshButton.isManaged());
        System.out.println("Label visible: " + messageLabel.isVisible());
        System.out.println("Label managed: " + messageLabel.isManaged());
        parentController.refreshSheetToLatestVersion();

        System.out.println("Button visible: " + refreshButton.isVisible());
        System.out.println("Button managed: " + refreshButton.isManaged());
        System.out.println("Label visible: " + messageLabel.isVisible());
        System.out.println("Label managed: " + messageLabel.isManaged());

    }

    public void cancelTask() {
        messageRefresherTask.cancel();
        timer.cancel();
        hide();
    }

    private void hide() {
        messageLabel.setVisible(false);
        messageLabel.setManaged(false); // להבטיח שלא תתפוס מקום בפריסה
        messageLabel.setStyle("");

        refreshButton.setVisible(false);
        refreshButton.setManaged(false); // להבטיח שלא יתפוס מקום בפריסה
        refreshButton.setStyle("");
    }


    private void show() {
        messageLabel.setVisible(true);
        messageLabel.setManaged(true); // מחזירה את ה-Label לתוך ה-layout

        refreshButton.setVisible(true);
        refreshButton.setManaged(true); // מחזירה את ה-Button לתוך ה-layout
    }

    public void setMainController(SheetManagerController sheetManagerController) {
        this.parentController = sheetManagerController;
    }

//    public String getSelectedSheetName() {
//        return selectedSheetName;
//    }
//
//    public void setParentController(MultiSheetsScreenController parentController) {
//        this.parentController = parentController;
//    }
}
