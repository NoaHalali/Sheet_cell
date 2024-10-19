package client.components.sheetManager.parts.top.actionLine;

import client.components.sheetManager.SheetManagerController;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import parts.cell.CellDTO;
import shticell.engines.engine.EngineImpl;

public class ActionLineController {

    private SheetManagerController mainController;

    @FXML
    private Label cellId;
    @FXML
    private Label cellOriginalValue;
    @FXML
    private Button updateCell;
    @FXML
    private Label cellLastUpdateVersion;
    @FXML
    private TextField updateCellValueField;

    private EngineImpl engine;

    public void initializeActionLine(SimpleBooleanProperty cellSelected, BooleanBinding hasEditPermission) {
        BooleanBinding cellSelectedAndHasEditPermission = cellSelected.and(hasEditPermission);

        updateCell.disableProperty().bind(cellSelectedAndHasEditPermission.not());
        updateCellValueField.disableProperty().bind(cellSelectedAndHasEditPermission.not());
    }

    public void setActionLine(CellDTO cell) {
        if (cell != null) {
            // עדכון השדות בשורת הפעולה לפי פרטי התא
            cellId.setText(cell.getCoord().toString());
            cellOriginalValue.setText(cell.getOriginalValue());
            if (cell.getLastUpdatedVersion() != 0) {
                cellLastUpdateVersion.setText(String.valueOf(cell.getLastUpdatedVersion()));
            } else {
                cellLastUpdateVersion.setText("");
            }
        } else {
            // איפוס השדות בשורת הפעולה אם אין תא ממוקד
            cellId.setText("");
            cellOriginalValue.setText("");
            cellLastUpdateVersion.setText("");
        }
    }

    public void updateCellButtonAction() {
        mainController.updateCellValue(updateCellValueField.getText());
    }


    public void setMainController(SheetManagerController mainController) {
        this.mainController = mainController;
    }

}



