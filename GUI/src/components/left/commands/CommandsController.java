package components.left.commands;

import components.MainComponent.AppController;
import components.center.cellsTable.TableController;
import components.left.commands.filter.FilterPopupController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import parts.SheetDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommandsController {

    private AppController mainController;

    @FXML
    private Button setColumnRowWidthButton;
    @FXML
    private Button setColumnAlignmentButton;
    @FXML
    private Button resetCellStyleButton;
    @FXML
    private TextField columnRowWidthTextField;
    @FXML
    private TextField columnAlignmentTextField;
    @FXML
    private ColorPicker textColorPicker;
    @FXML
    private ColorPicker backgroundColorPicker;
    @FXML
    private Button applyTextColorButton;
    @FXML
    private Button applyBackgroundColorButton;
    @FXML
    private Button displaySortButton;
    @FXML
    private TextField sortRangeTextField;
    @FXML
    private TextField sortColumnsTextField;

    @FXML
    private TextField filterRangeTextField;
    @FXML
    private TextField filterColumnsTextField;

    @FXML private Button calcValuesToFilterButton;
    @FXML
    private ListView<String> filterListView; // הוספת ListView לסינון
    @FXML
    private Button applyFilterButton; // כפתור לסינון

    private ObservableList<String> selectedItems = FXCollections.observableArrayList(); // רשימת פריטים נבחרים

    public void InitializeCommandsController(SimpleBooleanProperty isCellSelected) {
        resetCellStyleButton.disableProperty().bind(isCellSelected.not());
        textColorPicker.disableProperty().bind(isCellSelected.not());
        backgroundColorPicker.disableProperty().bind(isCellSelected.not());
        applyTextColorButton.disableProperty().bind(isCellSelected.not());
        applyBackgroundColorButton.disableProperty().bind(isCellSelected.not());

        // אתחול ListView לסינון
        initializeFilterListView();

        applyFilterButton.setOnAction(event -> applyFilterAction());
    }

    // פונקציה לאתחול ListView לסינון
    private void initializeFilterListView() {
        // יצירת רשימה של פריטים להוספה ל-ListView
        ObservableList<String> items = FXCollections.observableArrayList("Value 1", "Value 2", "Value 3", "Value 4");

        // הגדרת רשימת הפריטים ל-ListView
        filterListView.setItems(items);

        // יצירת CheckBox עבור כל פריט ברשימה
        filterListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            SimpleBooleanProperty selected = new SimpleBooleanProperty(false);

            // הוספת מאזין כדי לעדכן את רשימת הפריטים הנבחרים
            selected.addListener((obs, wasSelected, isNowSelected) -> {
                if (isNowSelected) {
                    selectedItems.add(item);
                } else {
                    selectedItems.remove(item);
                }
            });

            return selected;
        }));
    }

    @FXML
    private void applyFilterAction() {
        // יישום לוגיקת הסינון לפי הערכים שנבחרו
        mainController.filterData(selectedItems);
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
            Stage popupStage = new Stage();
            popupStage.setTitle("Sorting Preview: ");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/center/cellsTable/table.fxml"));
            Parent root = loader.load();

            TableController tableController = loader.getController();
            List<Character> colsList = colsStringToCharList(sortColumnsTextField.getText());
            SheetDTO sheet = mainController.getSortedSheetDTO(sortRangeTextField.getText(), colsList);
            Map<String, String> styleMap = mainController.getStylesFromMainSheet();
            tableController.showSheetPreview(sheet);
            tableController.updateCellsStyleAfterSort(styleMap, sheet);

            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();

        } catch (Exception e) {
            mainController.showAlert("Error", e.getMessage());
        }
    }

    private List<Character> colsStringToCharList(String input) {
        String[] charArray = input.toUpperCase().split(",");
        List<Character> charList = new ArrayList<>();

        for (String s : charArray) {
            if (!s.isEmpty()) {
                charList.add(s.charAt(0));
            }
        }
        return charList;
    }

    @FXML
    private void openFilterPopup() {
        try {
            // טוען את ה-FXML של הפופאפ
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/left/commands/filter/FilterPopup.fxml"));
            Parent root = loader.load();

            // קבל את הבקר של הפופאפ
            FilterPopupController filterPopupController = loader.getController();
            String col= filterColumnsTextField.getText();
            String rangeDefinition =filterRangeTextField.getText();
            Set<String> values=mainController.getDistinctValuesOfColInRange(col, rangeDefinition);
            filterPopupController.initializeFilterListView(values);

            // יצירת הבמה (Stage) לפופאפ
            Stage popupStage = new Stage();
            popupStage.setTitle("Filter Values");
            popupStage.setScene(new Scene(root));
            popupStage.initModality(Modality.APPLICATION_MODAL); // פופאפ במצב מודאלי

            // הצגת הפופאפ וחסימת חלון ראשי עד לסגירת הפופאפ
            popupStage.showAndWait();

            // קבלת הערכים שנבחרו מהפופאפ לאחר שהמשתמש סיים את הבחירה
            ObservableList<String> selectedItems = filterPopupController.getSelectedItems();
            System.out.println("Selected items from popup: " + selectedItems);

            // תוכל לקרוא לפונקציה filterData עם הערכים שנבחרו
            mainController.filterData(selectedItems);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
