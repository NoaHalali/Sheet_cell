package client.components.sheetManager.top.actionLine;

import client.components.sheetManager.main.SheetManagerController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import parts.cell.CellDTO;
import shticell.engine.impl.EngineImpl;

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

    public void initializeActionLine(SimpleBooleanProperty cellSelected) {
        updateCell.disableProperty().bind(cellSelected.not());
        updateCellValueField.disableProperty().bind(cellSelected.not());
//        updateCell.setOnAction(event -> {
//            try {
//                mainController.updateCellValue(updateCellValueField.getText());
//
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//
//        });

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



