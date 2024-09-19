package components.center.cell;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import parts.sheet.cell.coordinate.Coordinate;

public class CellController {

    @FXML
    private Label cellLabel;
    private String textColor;
    private String backgroundColor;
    private String alignment;
    private String borderColor;
    private String borderWidth;
    private final DropShadow hoverShadow = new DropShadow();
    private Coordinate coord;

    //Default style values
    private static final String DEFAULT_BACKGROUND_COLOR = "#f0f0f0";
    private static final String DEFAULT_TEXT_COLOR = "black";
    private static final String DEFAULT_ALIGNMENT = "center";
    private static final String DEFAULT_BORDER_COLOR = "black";
    private static final String DEFAULT_BORDER_WIDTH = "0.5px";

    public CellController() {
        hoverShadow.setColor(Color.LIGHTBLUE);
        hoverShadow.setRadius(10);
    }

    public void setText(String text) {
        cellLabel.setText(text);
    }

    public void setTextColor(String color) {
        textColor = "-fx-text-fill: " + color + ";";
        applyStyles();
    }
    public  Coordinate getCoord() {
      return coord;
    }
    public void setCoord(Coordinate coord) {
        this.coord = coord;
    }

    public void setBackgroundColor(String color) {
        backgroundColor = "-fx-background-color: " + color + ";";
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
        borderColor = "-fx-border-color: " + color + ";";
        applyStyles();
    }

    public void setBorderWidth(String width) {
        borderWidth = "-fx-border-width: " + width + ";";
        applyStyles();
    }

    public String copyPreviewStyle() {
        String style = buildStyleString() + "-fx-border-color: black; -fx-border-width: 0.5px;";
        return style;
    }


    public String buildStyleString() {
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
        cellLabel.setStyle(buildStyleString());
    }

    public void applyHoverEffectListeners() {
//        cellLabel.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> applyHoverEffect());
//        cellLabel.addEventHandler(MouseEvent.MOUSE_EXITED, event -> removeHoverEffect());
    }

//    private void applyHoverEffect() {
//        cellLabel.setEffect(hoverShadow);
//        String style = buildStyleString() + "-fx-border-color: blue; -fx-border-width: 2px; -fx-background-color: #f0f8ff;";
//        cellLabel.setStyle(style);
//    }

//private void removeHoverEffect() {
//    cellLabel.setEffect(null);
//    applyStyles();
//}

    public void setCellStyle(String style){
        cellLabel.setStyle(style);
    }

    public void resetStyle() {
        textColor = "-fx-text-fill: " + DEFAULT_TEXT_COLOR + ";";
        backgroundColor = "-fx-background-color: " + DEFAULT_BACKGROUND_COLOR + ";";
        //borderWidth = "-fx-border-width: " + DEFAULT_BORDER_WIDTH + ";";
        //alignment = "-fx-alignment: " + DEFAULT_ALIGNMENT + ";";
        //borderColor = "-fx-border-color: " + DEFAULT_BORDER_COLOR + ";";
        applyStyles();
    }

    public void setRangeHighlight() {
        String style = buildStyleString() + "-fx-border-color: #fdfd00; -fx-border-width: 2px;";
        cellLabel.setStyle(style);
    }

    public void clearRangeHighlight() {
        applyStyles();
    }

    public void setBorder(String color, String width) {
        borderColor = "-fx-border-color: " + color + ";";
        borderWidth = "-fx-border-width: " + width + ";";
        applyStyles();
    }

    public void resetBorder() {
        setBorder(DEFAULT_BORDER_COLOR, DEFAULT_BORDER_WIDTH);
        applyStyles();
    }

    public void setInitialStyle() {

        alignment = "-fx-alignment: " + DEFAULT_ALIGNMENT + ";";
        borderColor = "-fx-border-color: " + DEFAULT_BORDER_COLOR + ";";
        borderWidth = "-fx-border-width: " + DEFAULT_BORDER_WIDTH + ";";
        resetStyle();
    }
}
