package client.components.sheetManager.parts.top.updates;

import client.components.sheetManager.SheetManagerController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.Timer;
import java.util.TimerTask;

import static client.components.Utils.Constants.REFRESH_RATE;

public class SheetUpdatesController {

    @FXML private Label messageLabel;
    @FXML private Button refreshButton;

    private SheetManagerController parentController;
    String sheetName;
    private BooleanProperty hasNewUpdate = new SimpleBooleanProperty(false);

    private Timer timer;
    private TimerTask messageRefresherTask;
    private static final String NO_UPDATES = "";

    public void initializeSheetUpdatesController(String sheetName) {
        //messageLabel.setVisible(false);
        //refreshButton.setVisible(false);
        hasNewUpdate.set(false);
        this.sheetName = sheetName;
        startVersionUpdateMessageRefresher();
        refreshButton.disableProperty().bind(hasNewUpdate.not());
    }

    public void startVersionUpdateMessageRefresher() {

        messageRefresherTask = new SheetUpdatesRefresher(this::updateMessage,sheetName );
        timer = new Timer();
        timer.schedule(messageRefresherTask, REFRESH_RATE, 5000);
    }


    public void updateMessage(String message) {
        Platform.runLater(() -> {
            if (!message.equals(NO_UPDATES)) {
                hasNewUpdate.set(true); // הגדרת שיש עדכון חדש
                messageLabel.setText(message);
                //lastMessage = message;

                // עיצוב ה-label והכפתור לפי העדכון
                messageLabel.setStyle("-fx-background-color: #f2f273; -fx-text-fill: #000000;");
                //refreshButton.setDisable(false); // הפעלת הכפתור
                //TODO: maybe cancel the task while there is a new update, doing problems for now so passed on it
            }
        });
    }

    @FXML
    private void handleRefreshButtonClick() {
        parentController.refreshSheetToLatestVersion(); // רענון הגיליון
        setNoUpdatesMode();
    }

    public void cancelTask() {
        messageRefresherTask.cancel();
        timer.cancel();

        setNoUpdatesMode();
    }

    private void setNoUpdatesMode() {
        hasNewUpdate.set(false);
        messageLabel.setStyle(""); // איפוס הסטייל
        messageLabel.setText(""); // איפוס הטקסט
        hasNewUpdate.set(false);
    }

    public void setParentController(SheetManagerController sheetManagerController) {
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
