package client.components.multiSheetsScreen.permissionsTable;

import client.components.multiSheetsScreen.MultiSheetsScreenController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import parts.permission.UserRequestDTO;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static client.components.Utils.Constants.REFRESH_RATE;

public class PermissionsTableController {

    @FXML private TableView<UserRequestDTO> requestsTable;
    @FXML private TableColumn<UserRequestDTO, String> permissionTypeColumn;
    @FXML private TableColumn<UserRequestDTO, String> requestNumberColumn;
    @FXML private TableColumn<UserRequestDTO, String> requestStatusColumn;
    @FXML private TableColumn<UserRequestDTO, String> requesterNameColumn;

    private MultiSheetsScreenController parentController;
    private int selectedRequestNumber;

    private Timer timer;
    private TimerTask listRefresher;
    //private final IntegerProperty totalSheets;

    @FXML
    public void initialize() {
        permissionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("permission"));
        requestNumberColumn.setCellValueFactory(new PropertyValueFactory<>("requestNumber"));
        requestStatusColumn.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
        requesterNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));  // Add this if `permission` exists in DTO

        requestsTable.setRowFactory(tv -> {
            TableRow<UserRequestDTO> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    UserRequestDTO clickedRowData = row.getItem();  // קבלת הנתונים של השורה שנלחצה
                    System.out.println("request number chosen: " + clickedRowData.getRequestNumber() );
                    selectedRequestNumber = clickedRowData.getRequestNumber();
                    parentController.handleRequestSelect(clickedRowData.getRequestStatus());

                }
            });
            return row;
        });
    }
    private void updateRequestsList(List<UserRequestDTO> requestsList) {
        Platform.runLater(() -> {
            if (requestsList != null) {
                ObservableList<UserRequestDTO> items = requestsTable.getItems();
                items.clear(); // Clear the current table
                items.addAll(requestsList); // Add new DTO list to the table
            } else {
                System.err.println("requestsList is null, cannot update table.");
            }
        });
    }

    public void startListRefresher() {
        String selectedSheet = parentController.getSelectedSheetName();
        if (listRefresher != null) {
            listRefresher.cancel();
        }
        listRefresher = new RequestsListRefresher(this::updateRequestsList, selectedSheet);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

//    public String getSelectedSheetName() {
//        return selectedSheetName;
//    }

    public void setParentController(MultiSheetsScreenController parentController) {
        this.parentController = parentController;
    }
}
