package client.components.sheetManager.parts.left.commands;

import client.components.Utils.EffectiveValueUtils;
import client.components.Utils.StageUtils;
import client.components.sheetManager.left.commands.rowsAndCols.DialogManager;
import client.components.sheetManager.parts.center.cellsTable.TableController;
import client.components.sheetManager.parts.left.commands.filter.FilterPopupController;
import client.components.sheetManager.parts.left.commands.graph.GraphController;
import client.components.sheetManager.SheetManagerController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import parts.SheetDTO;
import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static client.components.Utils.Constants.FILTER_POPUP_FXML_RESOURCE_LOCATION;
import static client.components.Utils.Constants.TABLE_FXML_RESOURCE_LOCATION;


public class CommandsController {

    private SheetManagerController parentController;
    private DialogManager dialogManager = new DialogManager();

    @FXML private Button setColumnWidthButton;
    @FXML private Button setRowHeightButton;
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
    @FXML private TextField whatIfMinimumTextField;
    @FXML private TextField whatIfMaximumTextField;
    @FXML private Button calculateWhatIfButton;

    @FXML private TextField filterRangeTextField;
    @FXML private TextField filterColumnsTextField;
    @FXML private Slider whatIfSlider;
    @FXML private Label minimumValueSliderLabel;
    @FXML private Label maximumValueSliderLabel;
    @FXML private VBox setWhatIfSettingsVBox;
    @FXML private VBox SliderSettingsVBox;
    @FXML private Label whatIfCoordinateLabel;
    @FXML private Button calcValuesToFilterButton;
    @FXML private ListView<String> filterListView; // הוספת ListView לסינון
    @FXML private Button applyFilterButton; // כפתור לסינון

    @FXML private TextField xColRangeTextField;
    @FXML private TextField yColRangeTextField;
    @FXML private Button createGraphButton;

    public void InitializeCommandsController(SimpleBooleanProperty cellSelected, SimpleBooleanProperty columnSelected,
                                             SimpleBooleanProperty rowSelected, SimpleBooleanProperty showWhatIfMode, BooleanBinding hasEditPermission) {

        bindRowsColsStyle(columnSelected, rowSelected, hasEditPermission);
        bindCellStyleCommands(cellSelected, hasEditPermission);
        bindWhatIfCommands(cellSelected, showWhatIfMode);
        //sections disable in what if mode
        displaySortButton.disableProperty().bind(showWhatIfMode);
        calcValuesToFilterButton.disableProperty().bind(showWhatIfMode);
        createGraphButton.disableProperty().bind(showWhatIfMode);
    }

    @FXML
    public void applyTextColorAction() {
        Color selectedColor = textColorPicker.getValue();
        if (selectedColor != null) {
            parentController.setCellTextColor(selectedColor);
        }
    }

    @FXML
    public void applyBackgroundColorAction() {
        Color selectedColor = backgroundColorPicker.getValue();
        if (selectedColor != null) {
            parentController.setCellBackgroundColor(selectedColor);
        }
    }

    @FXML
    public void resetCellStyleAction() {
        parentController.resetCellStyle();
    }

    public void setParentController(SheetManagerController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void displaySortAction() {
        try {
            List<Character> colsList = colsStringToCharList(sortColumnsTextField.getText());

            parentController.getSortedSheetDTO(sortRangeTextField.getText(), colsList, sheet -> {
                try {
                    previewSheetDTOWithPrevStyleInPopup(sheet,"Sorted Sheet Preview");
                } catch (Exception e) {
                    StageUtils.showAlert("Error", e.getMessage());
                }
            });

        } catch (Exception e) {
            StageUtils.showAlert("Error", e.getMessage());
        }
    }

    public void previewSheetDTOWithPrevStyleInPopup(SheetDTO sheet,String popUpName) throws Exception {
        Stage popupStage = new Stage();
        popupStage.setTitle(popUpName);

        // טעינת FXML או יצירת ממשק באופן דינמי
        FXMLLoader loader = new FXMLLoader(getClass().getResource(TABLE_FXML_RESOURCE_LOCATION)); // Adjust the path
        Parent root = loader.load();

        // קבלת ה-TableController ואתחולו עם הגרסה הנבחרת של הגיליון
        TableController tableController = loader.getController();
        Map<String, String> styleMap = parentController.getStylesFromMainSheet();
        tableController.showSheetPreview(sheet);
        tableController.updateCellsStyleAfterSort(styleMap, sheet);


        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.showAndWait();

    }

    private List<Character> colsStringToCharList(String input) {
        String[] charArray = input.toUpperCase().split(",");
        validateColsSeperatedInput(input);
        List<Character> charList = new ArrayList<>();

        for (String s : charArray) {
            if (!s.isEmpty()) {
                charList.add(s.charAt(0));
            }
        }
        return charList;
    }

    public void validateColsSeperatedInput(String input) throws IllegalArgumentException {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Input cannot be empty."
            );
        }
        // Regular expression to validate letters (lowercase or uppercase) separated by commas
        String regex = "^[a-zA-Z](,[a-zA-Z])*$";

        // Check if the input matches the required format
        if (!input.matches(regex)) {
            throw new IllegalArgumentException(
                    "Input must be letters (a-z, A-Z) separated by ',' " +
                            "with no spaces. Example: 'a,b,C' or 'A,B,C'."
            );
        }
    }

    @FXML
    private void OnCalcValuesToFilterButtonClicked() {
        try {
            openFilterOptionsPopupAndGetFilteredSheet(sheet -> {
                // פתיחת פופאפ עם הגיליון המסונן);
                try {
                    previewSheetDTOWithPrevStyleInPopup(sheet, "Filtered Sheet Preview");
                } catch (Exception e) {
                    StageUtils.showAlert("Error", e.getMessage());
                }
            }, errorMessage -> {
                StageUtils.showAlert("Error", errorMessage);
            });
        } catch (Exception e) {
            StageUtils.showAlert("Error", e.getMessage());
        }

    }

    private void openFilterOptionsPopupAndGetFilteredSheet(Consumer<SheetDTO> onSuccess, Consumer<String> onFailure) throws Exception {
        List<Character> colsList = colsStringToCharList(filterColumnsTextField.getText());
        String rangeDefinition = filterRangeTextField.getText();

        // קריאה למתודה שמקבלת את הערכים הייחודיים
        parentController.getDistinctValuesOfMultipleColsInRange(colsList, rangeDefinition, values -> {
            // קריאה למתודה שתפתח את הפופאפים ותאפשר סינון
            Map<String, Set<EffectiveValue>> filteredValues = null;
            try {
                filteredValues = openMultipleFilterPopups(values);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // קריאה לפילטר נתונים בהתאם לערכים המסוננים
            parentController.filterData(filteredValues, rangeDefinition, filteredSheet -> {
                onSuccess.accept(filteredSheet);  // קריאה ל-onSuccess במקרה של הצלחה
            }, errorMessage -> {
                onFailure.accept(errorMessage);  // קריאה ל-onFailure במקרה של שגיאה
            });

        }, errorMessage -> {
            onFailure.accept(errorMessage);  // טיפול בשגיאה במקרה שלא ניתן לקבל את הערכים הייחודיים
        });
    }


    private  Map<String,Set<EffectiveValue>> openMultipleFilterPopups(Map<String,Set<EffectiveValue>> allValues) throws Exception {
        Map<String,Set<EffectiveValue>> filteredValues = new HashMap<>();
        for (String key : allValues.keySet()) {
            // טוען את ה-FXML של הפופאפ
            Set<EffectiveValue> valueSet=allValues.get(key);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FILTER_POPUP_FXML_RESOURCE_LOCATION));
            Parent root = loader.load();
            Map<String, EffectiveValue> stringToEffectiveValueMap = getStringToEffectiveValueMap(valueSet);
            Set<String> valuesSetStr = stringToEffectiveValueMap.keySet();

            // קבל את הבקר של הפופאפ
            FilterPopupController filterPopupController = loader.getController();
            filterPopupController.initializeFilterListView(valuesSetStr);

            // יצירת הבמה (Stage) לפופאפ
            Stage popupStage = new Stage();
            popupStage.setWidth(300);
            popupStage.setTitle("Filter Values for col - " + key);
            popupStage.setScene(new Scene(root));
            popupStage.initModality(Modality.APPLICATION_MODAL); // פופאפ במצב מודאלי

            // הצגת הפופאפ וחסימת חלון ראשי עד לסגירת הפופאפ
            popupStage.showAndWait();

            // קבלת הערכים שנבחרו מהפופאפ לאחר שהמשתמש סיים את הבחירה
            ObservableList<String> selectedItems = filterPopupController.getSelectedItems();
            Set<EffectiveValue> selectedValues = selectedItems.stream()
                    .map(stringToEffectiveValueMap::get)
                    .collect(Collectors.toSet());
           filteredValues.put(key,selectedValues);
        }
        return filteredValues;
    }

    public Map<String, EffectiveValue> getStringToEffectiveValueMap(Set<EffectiveValue> values) {
        return values.stream()
                .collect(Collectors.toMap(EffectiveValueUtils::calcValueToString, v -> v));
    }
    public Set<String> getStringSetFromEffectiveValues(Set<EffectiveValue> values) {
        return values.stream()
               .map(EffectiveValueUtils::calcValueToString) // המרה למחרוזת//todo להוסיף את CALCVALUE למקום אחר
               .collect(Collectors.toSet()); // המרת הזרם לסט
    }

    // פעולה לפתיחת דיאלוג לעיצוב עמודות
    public void showColumnWidthDialog() {
        int currentColumnWidth = parentController.getFocusedColumnWidth();
        dialogManager.showColumnWidthDialog(currentColumnWidth,
                width -> parentController.setColumnWidth(width)  // קריאה לפונקציה לעדכון רוחב עמודה
        );
    }

    // פעולה לפתיחת דיאלוג לעיצוב שורות
    public void showRowHeightDialog() {
        int currentRowHeight = parentController.getFocusedRowHeight();
        dialogManager.showRowHeightDialog(currentRowHeight,
                height -> parentController.setRowHeight(height)  // קריאה לפונקציה לעדכון גובה שורה
        );
    }
    // פעולה לפתיחת דיאלוג יישור עמודות
    public void showColumnAlignmentDialog() {
        dialogManager.showColumnAlignmentDialog(
                alignment -> parentController.setColumnAlignment(alignment)  // פונקציה לעדכון יישור עמודה
        );
    }

    @FXML
    private void OnCreateGraphButtonCliked()
    {
        try {
            GraphController graphsController = new GraphController();
            graphsController.setMainController(parentController);
            String xRange = xColRangeTextField.getText();
            String yRange = yColRangeTextField.getText();
            graphsController.createGraph(xRange, yRange);
        }
        catch (Exception e) {
            StageUtils.showAlert("Error", e.getMessage());
        }
    }

    @FXML
    private void handleCalculateWhatIf() {
    try {
        double min = Double.parseDouble(whatIfMinimumTextField.getText());
        double max = Double.parseDouble(whatIfMaximumTextField.getText());

        if (max <= min) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        // קריאה למתודה האסינכרונית עם Consumers
        parentController.setEngineInWhatIfMode(parentController.getCurrentlyFocusedCellCoord(),
                (success) -> { // מה לעשות במקרה של הצלחה
                    parentController.changeWhatIfMode(true);
                    Coordinate focusedCoord = parentController.getCurrentlyFocusedCellCoord();
                    whatIfCoordinateLabel.setText(focusedCoord.toString());
                    minimumValueSliderLabel.setText(whatIfMinimumTextField.getText());
                    maximumValueSliderLabel.setText(whatIfMaximumTextField.getText());
                    whatIfSlider.setMin(min);
                    whatIfSlider.setMax(max);
                },
                (errorMessage) -> { // מה לעשות במקרה של שגיאה
                    StageUtils.showAlert("Error", errorMessage);
                }
        );

    } catch (NumberFormatException e) {
        StageUtils.showAlert("Input Error", "Please enter valid numbers.");
    } catch (IllegalArgumentException e) {
        StageUtils.showAlert("Error", e.getMessage());
    } catch (Exception e) {
        StageUtils.showAlert("Error", e.getMessage());
    }
}

    @FXML
    private void handlesWhatIfSliderMove() {
        parentController.calculateWhatIfValueForCell(whatIfSlider.getValue());
    }

    public void handleExitWhatIfMode() {
        parentController.changeWhatIfMode(false);
        parentController.showCurrentSheet();
    }

    private void bindRowsColsStyle(SimpleBooleanProperty columnSelected, SimpleBooleanProperty rowSelected, BooleanBinding hasEditPermission) {

        BooleanBinding columnSelectedAndHasEditPermission = Bindings.and(columnSelected, hasEditPermission);
        BooleanBinding rowSelectedAndHasEditPermission = Bindings.and(rowSelected, hasEditPermission);

        setColumnWidthButton.disableProperty().bind(columnSelectedAndHasEditPermission.not());
        setColumnAlignmentButton.disableProperty().bind(columnSelectedAndHasEditPermission.not());
        setRowHeightButton.disableProperty().bind(rowSelectedAndHasEditPermission.not());
    }

    private void bindCellStyleCommands(SimpleBooleanProperty cellSelected, BooleanBinding hasEditPermission) {

        BooleanBinding hasEditPermissionAndCellSelected = Bindings.and(hasEditPermission,cellSelected);
        resetCellStyleButton.disableProperty().bind(hasEditPermissionAndCellSelected.not());
        textColorPicker.disableProperty().bind(hasEditPermissionAndCellSelected.not());
        backgroundColorPicker.disableProperty().bind(hasEditPermissionAndCellSelected.not());
        applyTextColorButton.disableProperty().bind(hasEditPermissionAndCellSelected.not());
        applyBackgroundColorButton.disableProperty().bind(hasEditPermissionAndCellSelected.not());
    }

    private void bindWhatIfCommands(SimpleBooleanProperty cellSelected, SimpleBooleanProperty showWhatIfMode) {
        calculateWhatIfButton.disableProperty().bind(cellSelected.not());
        setWhatIfSettingsVBox.visibleProperty().bind(showWhatIfMode.not());
        setWhatIfSettingsVBox.managedProperty().bind(showWhatIfMode.not());
        whatIfSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            handlesWhatIfSliderMove();
        });
        SliderSettingsVBox.visibleProperty().bind(showWhatIfMode);
        SliderSettingsVBox.managedProperty().bind(showWhatIfMode);
    }

    @FXML
    public void setColumnRowWidthAction() {
        String width = columnRowWidthTextField.getText();
        // לוגיקה להגדרת רוחב העמודות/שורות והטיפול ב-wrap/clip
    }
}
