package components.center.cell;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CellController {

    @FXML
    private Label cellLabel;

    private String backgroundColor;
    private String alignment;
    private String borderColor;

    // כאן ניתן להוסיף לוגיקה לשינוי התא
    public void setText(String text) {
        cellLabel.setText(text);
    }

    public void setBackgroundColor(String color) {
        this.backgroundColor = "-fx-background-color: " + color + ";";
        applyStyles();
    }

    public void setAlignment(String alignment) {
        switch (alignment) {
            case "center":
                this.alignment = "-fx-alignment: center;";
                break;
            case "left":
                this.alignment = "-fx-alignment: center-left;";
                break;
            case "right":
                this.alignment = "-fx-alignment: center-right;";
                break;
        }
        applyStyles();
    }

    public void setBorderColor(String color) {
        this.borderColor = "-fx-border-color: " + color + ";";
        applyStyles();
    }

    private void applyStyles() {
        StringBuilder style = new StringBuilder();

        if (backgroundColor != null) {
            style.append(backgroundColor);
        }

        if (alignment != null) {
            style.append(alignment);
        }

        if (borderColor != null) {
            style.append(borderColor);
            style.append("-fx-border-width: 0.5px;");
        }

        cellLabel.setStyle(style.toString());
    }
}
