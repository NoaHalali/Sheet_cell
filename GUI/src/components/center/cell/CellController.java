package components.center.cell;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class CellController {

    @FXML
    private Label cellLabel;
    private String textColor;
    private String backgroundColor;
    private String alignment;
    private String borderColor;
    private String borderWidth; // משתנה חדש לשמירת רוחב המסגרת
    // הגדרת הצל עבור אפקט hover
    private final DropShadow hoverShadow = new DropShadow();

    public CellController() {
        // הגדרת הצל להוספה ב-hover
        hoverShadow.setColor(Color.LIGHTBLUE);
        hoverShadow.setRadius(10);
    }

    // כאן ניתן להוסיף לוגיקה לשינוי התא
    public void setText(String text) {
        cellLabel.setText(text);
    }

//    public void setTextColor(String color) {
//        cellLabel.setTextFill(color);
//        applyStyles();
//    }
    public void setTextColor(String color) {
        this.textColor = "-fx-text-fill: " + color + ";";
        applyStyles();
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

    // מתודה פרטית לבניית מחרוזת הסגנונות
    private String buildStyleString() {
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
        if (textColor != null) {
            style.append(textColor);
        }

        return style.toString();
    }

    private void applyStyles() {
        // שימוש במחרוזת הבנויה ליישום הסגנון
        cellLabel.setStyle(buildStyleString());
    }

    public void applyHoverEffectListeners() {
        // מאזין לאירוע ריחוף עם העכבר
        cellLabel.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> applyHoverEffect());

        // מאזין לאירוע יציאה עם העכבר
        cellLabel.addEventHandler(MouseEvent.MOUSE_EXITED, event -> removeHoverEffect());
    }

    private void applyHoverEffect() {
        cellLabel.setEffect(hoverShadow); // הוספת אפקט הצל

        // הוספת הסגנונות החדשים למחרוזת הזמנית
        String style = buildStyleString() + "-fx-border-color: blue; -fx-border-width: 2px; -fx-background-color: #f0f8ff;";

        // הגדרת הסגנון החדש עם כל השינויים
        cellLabel.setStyle(style);
    }

    private void removeHoverEffect() {
        cellLabel.setEffect(null); // הסרת אפקט הצל

        // חזרה לעיצוב המקורי על ידי יישום הסגנונות הקיימים בלבד
        applyStyles();
    }
}
