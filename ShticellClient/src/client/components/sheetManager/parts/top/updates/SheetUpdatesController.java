package client.components.sheetManager.parts.top.updates;

import client.components.multiSheetsScreen.MultiSheetsScreenController;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

import static client.components.Utils.Constants.REFRESH_RATE;

public class SheetUpdatesController {

    @FXML
    private Label messageLabel;
    private String lastMessage = ""; // משתנה לשמירת ההודעה האחרונה

    private MultiSheetsScreenController parentController;

    private Timer timer;
    private TimerTask messageRefresher;


    @FXML
    public void initialize() {

    }

//    private void updateMessage(String message) {
//        Platform.runLater(() -> {
//        messageLabel.setText(message);
//        });
//    }


    private void updateMessage(String message) {
        Platform.runLater(() -> {
            // בדיקה אם ההודעה השתנתה
            if (!message.equals(lastMessage)) {
                // הצגת ההודעה
                messageLabel.setVisible(true);
                messageLabel.setText(message);
                lastMessage = message; // עדכון ההודעה האחרונה

                // CSS להתראה
                messageLabel.setStyle("-fx-background-color: #5279bb; -fx-text-fill: #000000;");

                // אפקט הבהוב
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), messageLabel);
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.2);
                fadeTransition.setCycleCount(6); // 6 חזרות של הבהוב
                fadeTransition.setAutoReverse(true);

                // כאשר האפקט מסתיים, ההודעה נעלמת
                fadeTransition.setOnFinished(e -> {
                    messageLabel.setVisible(false); // הסתרת ההודעה
                    messageLabel.setText(""); // איפוס הטקסט
                    lastMessage = "";
                });

                fadeTransition.play();
            }
        });
    }


    public void startMessageRefresher() {

        messageRefresher = new SheetUpdatesRefresher(this::updateMessage);
        timer = new Timer();
        timer.schedule(messageRefresher, REFRESH_RATE, 5000);
    }

//    public String getSelectedSheetName() {
//        return selectedSheetName;
//    }
//
//    public void setParentController(MultiSheetsScreenController parentController) {
//        this.parentController = parentController;
//    }
}
