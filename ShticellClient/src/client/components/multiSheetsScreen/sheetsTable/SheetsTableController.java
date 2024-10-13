package client.components.multiSheetsScreen.sheetsTable;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import parts.SheetDetailsDTO;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static client.components.Utils.Constants.REFRESH_RATE;

public class SheetsTableController {

    private Timer timer;
    private TimerTask listRefresher;
    private final IntegerProperty totalSheets;

    @FXML private TableView<SheetDetailsDTO> sheetsTable;
    @FXML private TableColumn<SheetDetailsDTO, String> sheetNameColumn;
    @FXML private TableColumn<SheetDetailsDTO, String> ownerNameColumn;
    @FXML private TableColumn<SheetDetailsDTO, String> sheetSizeColumn;
   // @FXML private TableColumn<SheetDetailsDTO, String> permissionColumn;

    public SheetsTableController() {
        totalSheets = new SimpleIntegerProperty();
    }

    @FXML
    public void initialize() {
        sheetNameColumn.setCellValueFactory(new PropertyValueFactory<>("sheetName"));
        sheetSizeColumn.setCellValueFactory(new PropertyValueFactory<>("sheetSize"));
        ownerNameColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
       // permissionColumn.setCellValueFactory(new PropertyValueFactory<>("permission"));  // Add this if `permission` exists in DTO
    }

    private void updateUsersList(List<SheetDetailsDTO> sheetsDetailsNames) {
        Platform.runLater(() -> {
            ObservableList<SheetDetailsDTO> items = sheetsTable.getItems();
            items.clear(); // Clear the current table
            items.addAll(sheetsDetailsNames); // Add new DTO list to the table
        });
    }

    public void startListRefresher() {
        listRefresher = new SheetsListRefresher(this::updateUsersList);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }
}
