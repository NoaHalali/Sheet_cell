package components.left.commands;

import components.MainComponent.AppController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CommandsController {

    private AppController mainController;

    @FXML private Button setColumnRowWidthButton;
    @FXML private Button setColumnAlignmentButton;
    @FXML private Button setCellStyleButton;
    @FXML private Button resetCellStyleButton;

    @FXML
    public void setColumnRowWidthAction() {
        // לוגיקה להגדרת רוחב העמודות/שורות והטיפול ב-wrap/clip
    }

    @FXML
    public void setColumnAlignmentAction() {
        // לוגיקה לבחירת יישור של התוכן בעמודה
    }

    @FXML
    public void setCellStyleAction() {
        // לוגיקה לבחירת עיצוב של תא מסוים עם Color Picker
    }

    @FXML
    public void resetCellStyleAction() {
        // לוגיקה לביטול העיצוב של תא מסוים
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }
}
