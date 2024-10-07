package client.components.sheetManager.parts.left.ranges;

import client.components.Utils.StageUtils;
import client.components.sheetManager.SheetManagerController;
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

    private SheetManagerController mainController;
    private String lastSelectedRange = null; // שמירת שם הטווח שנבחר לאחרונה

    public void initializeRangesController(List<String> existingRanges, SimpleBooleanProperty rangeSelected) {
        refreshMenuButton(existingRanges);
        deleteRangeButton.disableProperty().bind(rangeSelected.not());
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
            mainController.addRange(rangeName, rangeDefinition);
            refreshMenuButton(mainController.getRanges());
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
            mainController.handleDeleteRange(lastSelectedRange);
            refreshMenuButton(mainController.getRanges());
            clearSelectedRangeOption();

        } catch (Exception e) {
            StageUtils.showAlert("Error", e.getMessage());
            clearSelectedRangeOption();
        }
    }

    private void handleRangeSelection(MenuItem selectedItem) {
        String rangeName = selectedItem.getText();

        if ("None".equals(rangeName)) {
            if(lastSelectedRange != null) {
                mainController.clearBorderMarkOfCells(); // ניקוי סימוני תאים קודם
                mainController.rangeSelectedProperty().set(false); // עדכון מצב בחירת טווח
                clearSelectedRangeOption();
            }
            else{
            }
            return;
        }

        if (rangeName != null && !"None".equals(rangeName)) {
            lastSelectedRange = rangeName; // שמירת הטווח הנבחר
            mainController.handleRangeSelection(rangeName);
//            mainController.clearBorderMarkOfCells(); // ניקוי סימוני תאים קודם
//            mainController.highlightRange(rangeName);

            //mainController.rangeSelectedProperty().set(true); // עדכון מצב בחירת טווח
        }
    }

    public void clearSelectedRangeOption() {
        lastSelectedRange = null; // איפוס שם הטווח שנבחר לאחרונה
        rangeMenuButton.setText("Choose range: "); // איפוס שם ה-MenuButton
        //deleteRangeButton.disableProperty()

    }

    public void setMainController(SheetManagerController mainController) {
        this.mainController = mainController;
    }
}
