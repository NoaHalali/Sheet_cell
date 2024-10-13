package client.components.sheetManager.parts.left.commands;

import client.components.Utils.EffectiveValueUtils;
import client.components.Utils.StageUtils;
import client.components.sheetManager.left.commands.rowsAndCols.DialogManager;
import client.components.sheetManager.parts.center.cellsTable.TableController;
import client.components.sheetManager.parts.left.commands.filter.FilterPopupController;
import client.components.sheetManager.parts.left.commands.graph.GraphController;
import client.components.sheetManager.SheetManagerController;
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

    private SheetManagerController mainController;
    private DialogManager dialogManager = new DialogManager();
    //private SimpleBooleanProperty showWhatIfSlider = new SimpleBooleanProperty(false);

    //@FXML private Button setColumnRowWidthButton;
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
//    @FXML private Label minimumSelectionNumberLabel;
//    @FXML private Label maximumSelectionNumberLabel;
    //@FXML private GridPane graphs;
   // @FXML private GraphsController graphsController;

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

    public void InitializeCommandsController(SimpleBooleanProperty cellSelected, SimpleBooleanProperty rangeSelected,
                                             SimpleBooleanProperty columnSelected, SimpleBooleanProperty rowSelected,SimpleBooleanProperty showWhatIfMode) {
        setColumnWidthButton.disableProperty().bind(columnSelected.not());
        setRowHeightButton.disableProperty().bind(rowSelected.not());
        setColumnAlignmentButton.disableProperty().bind(columnSelected.not());
        resetCellStyleButton.disableProperty().bind(cellSelected.not());
        textColorPicker.disableProperty().bind(cellSelected.not());
        backgroundColorPicker.disableProperty().bind(cellSelected.not());
        applyTextColorButton.disableProperty().bind(cellSelected.not());
        applyBackgroundColorButton.disableProperty().bind(cellSelected.not());
        calculateWhatIfButton.disableProperty().bind(cellSelected.not());
        SliderSettingsVBox.visibleProperty().bind(showWhatIfMode);
        SliderSettingsVBox.managedProperty().bind(showWhatIfMode);

        setWhatIfSettingsVBox.visibleProperty().bind(showWhatIfMode.not());
        setWhatIfSettingsVBox.managedProperty().bind(showWhatIfMode.not());
        whatIfSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            handlesWhatIfSliderMove();
        });
        //sections disable in what if mode
        displaySortButton.disableProperty().bind(showWhatIfMode);
        calcValuesToFilterButton.disableProperty().bind(showWhatIfMode);
        createGraphButton.disableProperty().bind(showWhatIfMode);

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

    public void setMainController(SheetManagerController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void displaySortAction() {
        try {
            List<Character> colsList = colsStringToCharList(sortColumnsTextField.getText());

            mainController.getSortedSheetDTO(sortRangeTextField.getText(), colsList, sheet -> {
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
        Map<String, String> styleMap = mainController.getStylesFromMainSheet();
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


//private SheetDTO openFilterOptionsPopupAndGetFilteredSheet() throws Exception {
//    List<Character> colsList = colsStringToCharList(filterColumnsTextField.getText());
//    String rangeDefinition = filterRangeTextField.getText();
//
//    Map<String,Set<EffectiveValue>> values = mainController.getDistinctValuesOfMultipleColsInRange(colsList, rangeDefinition);
//
//    // קריאה למתודה שתפתח מספר פופאפים
//    Map<String,Set<EffectiveValue>> filteredValues = openMultipleFilterPopups(values);
//    // החזרת ערך מתאים
//    return mainController.filterData(filteredValues, rangeDefinition);
//}

//        private SheetDTO openFilterOptionsPopupAndGetFilteredSheet() throws Exception {
//            return openFilterOptionsPopupAndGetFilteredSheet(
//                    sheet -> {
//                        // פתיחת פופאפ עם הגיליון המסונן
//                        try {
//                            previewSheetDTOWithPrevStyleInPopup(sheet,"Filtered Sheet Preview");
//                        } catch (Exception e) {
//                            StageUtils.showAlert("Error", e.getMessage());
//                        }
//                    },
//                    errorMessage -> {
//                        StageUtils.showAlert("Error", errorMessage);
//                    }
//            );
//
    private void openFilterOptionsPopupAndGetFilteredSheet(Consumer<SheetDTO> onSuccess, Consumer<String> onFailure) throws Exception {
        List<Character> colsList = colsStringToCharList(filterColumnsTextField.getText());
        String rangeDefinition = filterRangeTextField.getText();

        // קריאה למתודה שמקבלת את הערכים הייחודיים
        mainController.getDistinctValuesOfMultipleColsInRange(colsList, rangeDefinition, values -> {
            // קריאה למתודה שתפתח את הפופאפים ותאפשר סינון
            Map<String, Set<EffectiveValue>> filteredValues = null;
            try {
                filteredValues = openMultipleFilterPopups(values);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // קריאה לפילטר נתונים בהתאם לערכים המסוננים
            mainController.filterData(filteredValues, rangeDefinition, filteredSheet -> {
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
            // תוכל לקרוא לפונקציה filterData עם הערכים שנבחרו
            //mainController.filterData(selectedValues, col, rangeDefinition);
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
        int currentColumnWidth = mainController.getFocusedColumnWidth();
        dialogManager.showColumnWidthDialog(currentColumnWidth,
                width -> mainController.setColumnWidth(width)  // קריאה לפונקציה לעדכון רוחב עמודה
        );
    }

    // פעולה לפתיחת דיאלוג לעיצוב שורות
    public void showRowHeightDialog() {
        int currentRowHeight = mainController.getFocusedRowHeight();
        dialogManager.showRowHeightDialog(currentRowHeight,
                height -> mainController.setRowHeight(height)  // קריאה לפונקציה לעדכון גובה שורה
        );
    }
    // פעולה לפתיחת דיאלוג יישור עמודות
    public void showColumnAlignmentDialog() {
        dialogManager.showColumnAlignmentDialog(
                alignment -> mainController.setColumnAlignment(alignment)  // פונקציה לעדכון יישור עמודה
        );
    }
//
//    @FXML
//    private void handleCreateGraph() {
//        int numOfColumns = mainController.getNumberOfColumns();
//
//        String [] columns = createColumnsArray(numOfColumns);
//        // מראה את הדיאלוג לבחירת העמודות ליצירת הגרף
//        dialogManager.showGraphDialog(this::createGraphFromDialog,columns);
//    }

    @FXML
    private void OnCreateGraphButtonCliked()
    {
        try {
            GraphController graphsController = new GraphController();
            graphsController.setMainController(mainController);
            String xRange = xColRangeTextField.getText();
            String yRange = yColRangeTextField.getText();
            graphsController.createGraph(xRange, yRange);
        }
        catch (Exception e) {
            StageUtils.showAlert("Error", e.getMessage());
        }
    }


//    private void createGraphFromDialog(String xColumn, String yColumn) {
//        // נוודא שיש לנו GraphController ונקרא לפונקציה ליצירת הגרף
//        GraphController graphController = new GraphController();  // אם יש דרך ליצור את זה ב-FXML, עדיף
//        graphController.setMainController(mainController);  // העברת ה-mainController ל-GraphController
//        graphController.createGraph(xColumn, yColumn);  // יצירת הגרף עם העמודות הנבחרות
//    }
@FXML
private void handleCalculateWhatIf() {
    try {
        double min = Double.parseDouble(whatIfMinimumTextField.getText());
        double max = Double.parseDouble(whatIfMaximumTextField.getText());

        if (max <= min) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        // קריאה למתודה האסינכרונית עם Consumers
        mainController.setEngineInWhatIfMode(mainController.getCurrentlyFocusedCellCoord(),
                (success) -> { // מה לעשות במקרה של הצלחה
                    mainController.changeWhatIfMode(true);
                    Coordinate focusedCoord = mainController.getCurrentlyFocusedCellCoord();
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
        mainController.calculateWhatIfValueForCell(whatIfSlider.getValue());
    }

    public void handleExitWhatIfMode() {
        mainController.changeWhatIfMode(false);
        mainController.showCurrentSheet();
    }
}
