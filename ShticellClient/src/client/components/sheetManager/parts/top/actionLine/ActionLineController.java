package client.components.sheetManager.parts.top.actionLine;

import client.components.sheetManager.SheetManagerController;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import parts.cell.CellDTO;
public class ActionLineController {

    private SheetManagerController parentController;

    @FXML private Label cellId;
    @FXML private Label cellOriginalValue;
    @FXML private Button updateCell;
    @FXML private Label cellLastUpdate;
    @FXML private TextField updateCellValueField;

    public void initializeActionLine(SimpleBooleanProperty cellSelected, BooleanBinding hasEditPermission) {
        BooleanBinding cellSelectedAndHasEditPermission = cellSelected.and(hasEditPermission);

        updateCell.disableProperty().bind(cellSelectedAndHasEditPermission.not());
        updateCellValueField.disableProperty().bind(cellSelectedAndHasEditPermission.not());
    }

    public void setActionLine(CellDTO cell) {
        if (cell != null) {
            cellId.setText(cell.getCoord().toString());
            cellOriginalValue.setText(cell.getOriginalValue());

            int lastUpdateVersion = cell.getLastUpdatedVersion();
            String lastUpdateMessage;
            if(lastUpdateVersion == 0) {
                lastUpdateMessage = "";
            } else if (lastUpdateVersion == 1) {
                lastUpdateMessage = "version 1" ;
            } else {
                lastUpdateMessage = "version " + lastUpdateVersion + ", by " + cell.getLastEditedBy();
            }
            cellLastUpdate.setText(lastUpdateMessage);

//            if (lastUpdateVersion != 0) {
//                cellLastUpdate.setText(" version" + lastUpdateVersion + ", by " + cell.getLastEditedBy());
//                cellLastUpdate.setText(String.valueOf(cell.getLastUpdatedVersion()));
//            } else {
//                cellLastUpdate.setText("");
//            }
        } else {
            // איפוס השדות בשורת הפעולה אם אין תא ממוקד
            cellId.setText("");
            cellOriginalValue.setText("");
            cellLastUpdate.setText("");
        }
    }

    public void updateCellButtonAction() {
        parentController.updateCellValue(updateCellValueField.getText());
    }

    public void setParentController(SheetManagerController mainController) {
        this.parentController = mainController;
    }
}