package client.components.multiSheetsScreen.sheetsTable;

import client.components.multiSheetsScreen.MultiSheetsScreenController;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import parts.SheetDetailsDTO;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static client.components.Utils.Constants.REFRESH_RATE;

public class SheetsTableController {


    @FXML private TableView<SheetDetailsDTO> sheetsTable;
    @FXML private TableColumn<SheetDetailsDTO, String> sheetNameColumn;
    @FXML private TableColumn<SheetDetailsDTO, String> ownerNameColumn;
    @FXML private TableColumn<SheetDetailsDTO, String> sheetSizeColumn;
    private String selectedSheetName;
    private MultiSheetsScreenController parentController;

   // @FXML private TableColumn<SheetDetailsDTO, String> permissionColumn;

    private Timer timer;
    private TimerTask listRefresher;
    private final IntegerProperty totalSheets;

    public SheetsTableController() {
        totalSheets = new SimpleIntegerProperty();
    }

    @FXML
    public void initialize() {
        sheetNameColumn.setCellValueFactory(new PropertyValueFactory<>("sheetName"));
        sheetSizeColumn.setCellValueFactory(new PropertyValueFactory<>("sheetSize"));
        ownerNameColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
       // permissionColumn.setCellValueFactory(new PropertyValueFactory<>("permission"));  // Add this if `permission` exists in DTO
        sheetsTable.setRowFactory(tv -> {
            TableRow<SheetDetailsDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    SheetDetailsDTO clickedRowData = row.getItem();  // קבלת הנתונים של השורה שנלחצה
                    System.out.println("Clicked row of sheet: " + clickedRowData.getSheetName());
                    selectedSheetName = clickedRowData.getSheetName();
                }
            });
            return row;
        });

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

    public String getSelectedSheetName() {
        return selectedSheetName;
    }

    public void setParentController(MultiSheetsScreenController parentController) {
        this.parentController = parentController;
    }
}
