package components.left.commands;

import components.MainComponent.AppController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

public class CommandsController {

    private AppController mainController;

    @FXML private Button setColumnRowWidthButton;
    @FXML private Button setColumnAlignmentButton;
    @FXML private Button changeTextColorButton;
    @FXML private Button changeBorderColorButton;
    @FXML private Button resetCellStyleButton;

    @FXML private TextField columnRowWidthTextField;
    @FXML private TextField columnAlignmentTextField;
    @FXML private ColorPicker textColorPicker;
    @FXML private ColorPicker borderColorPicker;
    public void InitializeCommandsController(SimpleBooleanProperty isCellSelected){
        changeTextColorButton.disableProperty().bind(
                Bindings.or(
                        Bindings.isNull(textColorPicker.valueProperty()),  // Color not selected
                        isCellSelected.not()  // No cell selected
                )
        );

        // Disable the changeBorderColorButton until a color is selected in borderColorPicker
        changeBorderColorButton.disableProperty().bind(
                Bindings.or(
                        Bindings.isNull(borderColorPicker.valueProperty()),  // Color not selected
                        isCellSelected.not()  // No cell selected
                )
        );
        resetCellStyleButton.disableProperty().bind(isCellSelected.not());
        textColorPicker.disableProperty().bind(isCellSelected.not());
        borderColorPicker.disableProperty().bind(isCellSelected.not());
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
    public void changeTextColorAction() {
        // לוגיקה לשינוי צבע הטקסט
    }

    @FXML
    public void changeBorderColorAction() {
        // לוגיקה לשינוי צבע הגבול
    }

    @FXML
    public void resetCellStyleAction() {
        // לוגיקה לביטול העיצוב של תא מסוים
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}
