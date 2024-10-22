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

import static client.components.Utils.Constants.PERMISSIONS_TABLE_REFRESH_RATE;
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
    private RequestsListRefresher listRefresher;
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
//    private void updateRequestsList(List<UserRequestDTO> requestsList) {
//        Platform.runLater(() -> {
//            if (requestsList != null) {
//                ObservableList<UserRequestDTO> items = requestsTable.getItems();
//                items.clear(); // Clear the current table
//                items.addAll(requestsList); // Add new DTO list to the table
//            } else {
//                System.err.println("requestsList is null, cannot update table.");
//            }
//        });
//    }

    private void updateRequestsList(List<UserRequestDTO> requestsList) {
        Platform.runLater(() -> {
            if (requestsList != null) {
                ObservableList<UserRequestDTO> items = requestsTable.getItems();
                items.clear(); // Clear the current table
                items.addAll(requestsList); // Add new DTO list to the table

                // אם יש אינדקס שמור מהבחירה הקודמת, נבחר את השורה באותו אינדקס
                int selectedRequestIndex = selectedRequestNumber-1;
                if (selectedRequestIndex!= -1 && selectedRequestIndex < requestsList.size()) {
                    requestsTable.getSelectionModel().select(selectedRequestIndex);
                }
            } else {
                System.err.println("requestsList is null, cannot update table.");
            }
        });
    }



    public void startListRefresher() {
        String selectedSheet = parentController.getSelectedSheetName();

        // איפוס הבחירה כשמחליפים גיליון
        selectedRequestNumber = -1; // או ערך מתאים אחר שמסמן שאין בחירה

        if (listRefresher == null) {
            listRefresher = new RequestsListRefresher(this::updateRequestsList, selectedSheet);
            timer = new Timer();
            timer.schedule(listRefresher, REFRESH_RATE, PERMISSIONS_TABLE_REFRESH_RATE);
        }
        else {
            listRefresher.setSheetName(selectedSheet);
        }
    }

//    public String getSelectedSheetName() {
//        return selectedSheetName;
//    }

    public void setParentController(MultiSheetsScreenController parentController) {
        this.parentController = parentController;
    }

    public int getSelectedRequestNumber() {
        return selectedRequestNumber;
    }

    public void clearSelection() {
        requestsTable.getSelectionModel().clearSelection();
    }
}
