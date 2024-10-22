package client.components.sheetManager.parts.top.updates;

import client.components.multiSheetsScreen.MultiSheetsScreenController;
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
    private MultiSheetsScreenController parentController;
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
            messageLabel.setVisible(true);
            messageLabel.setText(message);
            lastMessage = message;

            messageLabel.setStyle("-fx-background-color: #5279bb; -fx-text-fill: #000000;");

            refreshButton.setVisible(true);
        }
    });
    }

    @FXML
    private void handleRefreshButtonClick() {
        // לוגיקה לטעינת הגיליון מחדש
        System.out.println("Refreshing the sheet...");

        // הסתרת ההודעה והכפתור לאחר הרענון
        messageLabel.setVisible(false);
        refreshButton.setVisible(false);
    }

    public void cancelTask() {
        messageRefresherTask.cancel();
        timer.cancel();
    }

//    public String getSelectedSheetName() {
//        return selectedSheetName;
//    }
//
//    public void setParentController(MultiSheetsScreenController parentController) {
//        this.parentController = parentController;
//    }
}
