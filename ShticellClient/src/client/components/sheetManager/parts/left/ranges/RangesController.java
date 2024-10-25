package client.components.sheetManager.parts.left.ranges;

import client.components.Utils.StageUtils;
import client.components.sheetManager.SheetManagerController;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import java.util.List;

public class RangesController {

    @FXML private MenuButton rangeMenuButton;
    @FXML private TextField rangeNameField;
    @FXML private TextField rangeDefinitionField;
    @FXML private Button addRangeButton;
    @FXML private Button deleteRangeButton;

    private SheetManagerController parentController;
    private String lastSelectedRange = null; // שמירת שם הטווח שנבחר לאחרונה

    public void initializeRangesController(List<String> existingRanges, SimpleBooleanProperty rangeSelected, BooleanBinding hasEditorPermission) {
        BooleanBinding rangeSelectedAndHasEditorPermission = rangeSelected.and(hasEditorPermission);

        refreshMenuButton(existingRanges);
        addRangeButton.disableProperty().bind(hasEditorPermission.not());
        deleteRangeButton.disableProperty().bind(rangeSelectedAndHasEditorPermission.not());
        rangeDefinitionField.disableProperty().bind(hasEditorPermission.not());
        rangeNameField.disableProperty().bind(hasEditorPermission.not());
    }

    private void refreshMenuButton(List<String> ranges) {
        rangeMenuButton.getItems().clear();

        // הוספת פריט "None" לתפריט
        MenuItem noneItem = new MenuItem("None");
        noneItem.setOnAction(event -> {
            handleRangeSelection(noneItem);
        });
        rangeMenuButton.getItems().add(noneItem);

        // הוספת כל הפריטים האחרים לתפריט
        for (String range : ranges) {
            MenuItem item = new MenuItem(range);
            item.setOnAction(event -> {
                handleRangeSelection(item);
                rangeMenuButton.setText(range); // הצגת הטווח שנבחר על הכפתור
            });
            rangeMenuButton.getItems().add(item);
        }
    }

    @FXML
    public void addRangeButtonAction() {
        String rangeName = rangeNameField.getText();
        String rangeDefinition = rangeDefinitionField.getText();

        try {
            parentController.addRange(rangeName, rangeDefinition, rangeNames->{
                        refreshMenuButton(rangeNames);
                    });
        } catch (Exception e) {
            StageUtils.showAlert("Error", e.getMessage());
            clearSelectedRangeOption();
        }
    }

    @FXML
    public void deleteRangeAction() {
        if (lastSelectedRange == null || "None".equals(lastSelectedRange)) {
            StageUtils.showAlert("Error", "No range selected or cannot delete 'None'!");
            return;
        }

        try {
            parentController.handleDeleteRange(lastSelectedRange, rangeNames->{
                refreshMenuButton(rangeNames);
                clearSelectedRangeOption();
            });

        } catch (Exception e) {
            StageUtils.showAlert("Error", e.getMessage());
            clearSelectedRangeOption();
        }
    }

    private void handleRangeSelection(MenuItem selectedItem) {
        String rangeName = selectedItem.getText();

        if ("None".equals(rangeName)) {
            if(lastSelectedRange != null) {
                parentController.clearBorderMarkOfCells(); // ניקוי סימוני תאים קודם
                parentController.rangeSelectedProperty().set(false); // עדכון מצב בחירת טווח
                clearSelectedRangeOption();
            }
            return;
        }

        if (rangeName != null && !"None".equals(rangeName)) {
            lastSelectedRange = rangeName; // שמירת הטווח הנבחר
            parentController.handleRangeSelection(rangeName);
        }
    }

    public void clearSelectedRangeOption() {
        lastSelectedRange = null;
        rangeMenuButton.setText("Choose range: "); // איפוס שם ה-MenuButton
    }

    public void setParentController(SheetManagerController parentController) {
        this.parentController = parentController;
    }
}
