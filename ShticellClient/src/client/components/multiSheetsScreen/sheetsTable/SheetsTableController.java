package client.components.multiSheetsScreen.sheetsTable;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static client.components.Utils.Constants.REFRESH_RATE;

public class SheetsTableController {

    private Timer timer;
    private TimerTask listRefresher;
    private final IntegerProperty totalSheets;
    //private HttpStatusUpdate httpStatusUpdate;

    @FXML
    private ListView<String> usersListView;
    @FXML private Label chatUsersLabel;

    public SheetsTableController() {
        totalSheets = new SimpleIntegerProperty();
    }

    @FXML
    public void initialize() {
        chatUsersLabel.textProperty().bind(Bindings.concat("Available Sheets: (", totalSheets.asString(), ")"));
    }

//    public void setHttpStatusUpdate(HttpStatusUpdate httpStatusUpdate) {
//        this.httpStatusUpdate = httpStatusUpdate;
//
//    }

//    public BooleanProperty autoUpdatesProperty() {
//        return autoUpdate;
//    }

    private void updateUsersList(List<String> sheetsNames) {
        Platform.runLater(() -> {
            ObservableList<String> items = usersListView.getItems();
            items.clear();
            items.addAll(sheetsNames);
            totalSheets.set(sheetsNames.size());
        });
    }

    public void startListRefresher() {
        listRefresher = new SheetsListRefresher(
                this::updateUsersList);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

//    @Override
//    public void close() {
//        usersListView.getItems().clear();
//        totalUsers.set(0);
//        if (listRefresher != null && timer != null) {
//            listRefresher.cancel();
//            timer.cancel();
//        }
//    }
}
