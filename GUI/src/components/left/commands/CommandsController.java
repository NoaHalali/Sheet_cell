package components.left.commands;

import components.MainComponent.AppController;
import components.center.cellsTable.TableController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import parts.SheetDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandsController {

    private AppController mainController;

    @FXML private Button setColumnRowWidthButton;
    @FXML private Button setColumnAlignmentButton;
    @FXML private Button resetCellStyleButton;

    @FXML private TextField columnRowWidthTextField;
    @FXML private TextField columnAlignmentTextField;
    @FXML private ColorPicker textColorPicker;
    @FXML private ColorPicker backgroundColorPicker;
    @FXML private Button applyTextColorButton;
    @FXML private Button applyBackgroundColorButton;
    @FXML private Button displaySortButton;
    @FXML private TextField sortRangeTextField;
    @FXML private TextField sortColumnsTextField;


    private Color lastSelectedTextColor;
    private Color lastSelectedBackgroundColor;

    public void InitializeCommandsController(SimpleBooleanProperty isCellSelected) {
        resetCellStyleButton.disableProperty().bind(isCellSelected.not());
        textColorPicker.disableProperty().bind(isCellSelected.not());
        backgroundColorPicker.disableProperty().bind(isCellSelected.not());
        applyTextColorButton.disableProperty().bind(isCellSelected.not());
        applyBackgroundColorButton.disableProperty().bind(isCellSelected.not());

//        textColorPicker.setOnAction(event -> {
//            Color newColor = textColorPicker.getValue();
//            if (newColor != null) {
//                changeTextColor(newColor);
//                // אפשר גם לאפס פוקוס או לבצע פעולה נוספת
//            }
//        });
//
//        backgroundColorPicker.setOnAction(event -> {
//            Color newColor = backgroundColorPicker.getValue();
//            if (newColor != null) {
//                changeBackgroundColor(newColor);
//                // אפשר גם לאפס פוקוס או לבצע פעולה נוספת
//            }
//        });

    }


//    public void InitializeCommandsController(SimpleBooleanProperty isCellSelected){
//        resetCellStyleButton.disableProperty().bind(isCellSelected.not());
//        textColorPicker.disableProperty().bind(isCellSelected.not());
//        backgroundColorPicker.disableProperty().bind(isCellSelected.not());
//
//        textColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                changeTextColor(newValue);
//            }
//        });
//
//        // הוספת מאזין לבחירת צבע גבול
//        backgroundColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                changeBackgroundColor(newValue);
//            }
//        });
//    }

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

//    @FXML
//    public void changeTextColor(Color color) {
//        mainController.setCellTextColor(color);
//    }
//
//    @FXML
//    public void changeBackgroundColor(Color color) {
//        mainController.setCellBackgroundColor(color);
//    }

    @FXML
    public void applyTextColorAction() {
        Color selectedColor = textColorPicker.getValue();
        if (selectedColor != null) {
            mainController.setCellTextColor(selectedColor);
        }
    }

    @FXML
    public void applyBackgroundColorAction() {
        Color selectedColor = backgroundColorPicker.getValue();
        if (selectedColor != null) {
            mainController.setCellBackgroundColor(selectedColor);
        }
    }


    @FXML
    public void resetCellStyleAction() {
        mainController.resetCellStyle();
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void displaySortAction() {

        try {
            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.setTitle("Sorting Preview: ");

            // Load the FXML or create a layout dynamically
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/center/cellsTable/table.fxml")); // Adjust the path
            Parent root = loader.load();

            TableController tableController = loader.getController();
            List<Character> colsList = colsStringToCharList(sortColumnsTextField.getText());
            SheetDTO sheet = mainController.getSortedSheetDTO(sortRangeTextField.getText(), colsList);
            Map<String,String> styleMap=mainController.getStylesFromMainSheet();
            tableController.showSheetPreview(sheet);
            tableController.updateCellsStyleAfterSort(styleMap,sheet);

            // Set the scene and show the popup
            Scene scene = new Scene(root);
            popupStage.setScene(scene);

            // Set the popup to block the main window (modality)
            popupStage.initModality(Modality.APPLICATION_MODAL);

            // Show the popup and wait for it to be closed before returning to the main window
            popupStage.showAndWait();

        } catch (Exception e) {
            mainController.showAlert("Error", e.getMessage());
        }
    }

    //For now here, maybe move to logic
    private List<Character> colsStringToCharList(String input) {
        String[] charArray = input.toUpperCase().split(",");
        List<Character> charList = new ArrayList<>();

        // המרה לרשימת תווים
        for (String s : charArray) {
            if (!s.isEmpty()) {
                charList.add(s.charAt(0));
            }
        }
        return charList;
    }





}
