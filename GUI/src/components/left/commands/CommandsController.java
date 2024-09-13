package components.left.commands;

import components.MainComponent.AppController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class CommandsController {

    private AppController mainController;

    @FXML private Button setColumnRowWidthButton;
    @FXML private Button setColumnAlignmentButton;
    @FXML private Button resetCellStyleButton;

    @FXML private TextField columnRowWidthTextField;
    @FXML private TextField columnAlignmentTextField;
    @FXML private ColorPicker textColorPicker;
    @FXML private ColorPicker backgroundColorPicker;
    public void InitializeCommandsController(SimpleBooleanProperty isCellSelected){
        resetCellStyleButton.disableProperty().bind(isCellSelected.not());
        textColorPicker.disableProperty().bind(isCellSelected.not());
        backgroundColorPicker.disableProperty().bind(isCellSelected.not());

        textColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                changeTextColor(newValue);
            }
        });

        // הוספת מאזין לבחירת צבע גבול
        backgroundColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                changeBackgroundColor(newValue);
            }
        });
    }

    @FXML
    public void setColumnRowWidthAction() {
        String width = columnRowWidthTextField.getText();
        // לוגיקה להגדרת רוחב העמודות/שורות והטיפול ב-wrap/clip
    }

    @FXML
    public void setColumnAlignmentAction() {
        String alignment = columnAlignmentTextField.getText();
        // לוגיקה לבחירת יישור של התוכן בעמודה
    }

    @FXML
    public void changeTextColor(Color color) {
        mainController.setCellTextColor(color);
    }

    @FXML
    public void changeBackgroundColor(Color color) {
        mainController.setCellBackgroundColor(color);
    }

    @FXML
    public void resetCellStyleAction() {
        mainController.resetCellStyle();
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}
