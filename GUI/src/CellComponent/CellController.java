package CellComponent;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CellController {
    @FXML
    private Label effectiveValue;

    public void setEffectiveValue(String effectiveValue) {
        this.effectiveValue.setText(effectiveValue);
    }

}
