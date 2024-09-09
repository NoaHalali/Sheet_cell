package components.center.cell;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CellController {

    @FXML
    private Label cellLabel;

    private String backgroundColor;
    private String alignment;
    private String borderColor;
    private String borderWidth; // משתנה חדש לשמירת רוחב המסגרת

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

    // מתודה חדשה להגדרת רוחב המסגרת
    public void setBorderWidth(String width) {
        this.borderWidth = "-fx-border-width: " + width + ";";
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
        }

        if (borderWidth != null) {
            style.append(borderWidth);
        }

        cellLabel.setStyle(style.toString());
    }
}
