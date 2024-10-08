package components.left.commands.rowsAndCols;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DialogManager {

    public void showColumnWidthDialog(int currentColumnWidth ,Consumer<Integer> setColumnWidth) {
        showDialog(currentColumnWidth,setColumnWidth, "Set Column Width", "Column Width");
//        Dialog<ButtonType> dialog = new Dialog<>();
//        dialog.setTitle("Set Column Width");
//
//        GridPane dialogGrid = new GridPane();
//        dialogGrid.setHgap(10);
//        dialogGrid.setVgap(10);
//
//        TextField columnWidthField = new TextField();
//        columnWidthField.setPromptText("Column Width");
//
//        dialogGrid.add(new Label("Column Width:"), 0, 0);
//        dialogGrid.add(columnWidthField, 1, 0);
//
//        dialog.getDialogPane().setContent(dialogGrid);
//        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//
//        dialog.setResultConverter(button -> {
//            if (button == ButtonType.OK) {
//                double columnWidth = Double.parseDouble(columnWidthField.getText());
//                setColumnWidth.accept(columnWidth);  // קריאה לפונקציה לעדכון רוחב עמודה
//            }
//            return null;
//        });
//
//        dialog.showAndWait();
    }

    public void showRowHeightDialog(int currentRowHeight, Consumer<Integer> setRowHeight) {

        showDialog(currentRowHeight, setRowHeight, "Set Row Height", "Row Height");
    }

    public void showDialog(int currentValue, Consumer<Integer> setMeasureMethod, String dialogTitle, String dialogMessageInfo) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);

        GridPane dialogGrid = new GridPane();
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);

        final int minValue = 1;
        final int maxValue = 100;
        final int initialValue = currentValue;
        int step = 1;
        Spinner<Integer> spinner = createMeasureSpinner(minValue, maxValue, initialValue, step);
        // Label עבור הודעת שגיאה
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");  // הגדרת צבע הטקסט לאדום, בהתחלה מוסתר

        dialogGrid.add(new Label(dialogMessageInfo), 0, 0);
        dialogGrid.add(spinner, 1, 0);
        dialogGrid.add(errorLabel, 0, 2, 2, 1);  // להוסיף את הודעת השגיאה לרשת, שורה 2

        dialog.getDialogPane().setContent(dialogGrid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextFormatter<Integer> textFormatter = createMeasureIntegerTextFormatter(minValue, errorLabel);

        // קישור ה-TextFormatter ל-TextField של ה-Spinner
        spinner.getEditor().setTextFormatter(textFormatter);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                int columnWidth = spinner.getValue();
                setMeasureMethod.accept(columnWidth);  // קריאה לפונקציה לעדכן את רוחב העמודה
            }
            return null;
        });

        dialog.showAndWait();
    }

    public void showColumnAlignmentDialog(Consumer<String> setColumnAlignment) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Set Column Alignment");

        GridPane dialogGrid = new GridPane();
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);

        // יצירת ComboBox עם אפשרויות יישור
        ComboBox<String> alignmentComboBox = new ComboBox<>();
        alignmentComboBox.getItems().addAll("LEFT", "CENTER", "RIGHT");
        alignmentComboBox.setPromptText("Choose Alignment");

        dialogGrid.add(new Label("Column Alignment:"), 0, 0);
        dialogGrid.add(alignmentComboBox, 1, 0);

        dialog.getDialogPane().setContent(dialogGrid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                String alignment = alignmentComboBox.getValue();
                if (alignment != null) {
                    setColumnAlignment.accept(alignment); // קריאה לפונקציה לעדכון היישור
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private Spinner<Integer> createMeasureSpinner(int minValue, int maxValue, int initialValue, int step) {
        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, initialValue, step);
        spinner.setValueFactory(valueFactory);
        spinner.setEditable(true);  // Allow user to enter value manually
        return spinner;
    }

    private TextFormatter<Integer> createMeasureIntegerTextFormatter(int minValue, Label errorLabel) {
        return new TextFormatter<>(c -> {
            if (c.getControlNewText().matches("-?\\d*")) { // לאפשר רק מספרים
                try {
                    int newValue = Integer.parseInt(c.getControlNewText());
                    if (newValue < minValue) {
                        errorLabel.setText("Value cannot be less than " + minValue);  // הצגת הודעת שגיאה
                        return null;  // לא להחיל את השינוי
                    }
                    errorLabel.setText("");  // איפוס הודעת השגיאה אם הכל תקין
                    return c;  // הערך חוקי
                } catch (NumberFormatException e) {
                    errorLabel.setText("Input must be a positive integer.");  // הצגת הודעת שגיאה
                    return null;  // ערך לא חוקי, לא להחיל את השינוי
                }
            } else {
                errorLabel.setText("Input must be a positive integer.");  // הצגת הודעת שגיאה
                return null;  // מחרוזת לא חוקית
            }
        });
    }

    public void showGraphDialog(BiConsumer<String, String> onGraphCreate, String[] columns) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create Graph");

        GridPane dialogGrid = new GridPane();
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);

        ComboBox<String> xAxisComboBox = new ComboBox<>();
        ComboBox<String> yAxisComboBox = new ComboBox<>();
        xAxisComboBox.getItems().addAll(columns);  // כאן תכניס את שמות העמודות
        yAxisComboBox.getItems().addAll(columns);  // כאן תכניס את שמות העמודות

        dialogGrid.add(new Label("X Axis:"), 0, 0);
        dialogGrid.add(xAxisComboBox, 1, 0);
        dialogGrid.add(new Label("Y Axis:"), 0, 1);
        dialogGrid.add(yAxisComboBox, 1, 1);

        dialog.getDialogPane().setContent(dialogGrid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                String selectedX = xAxisComboBox.getValue();
                String selectedY = yAxisComboBox.getValue();
                if (selectedX != null && selectedY != null) {
                    onGraphCreate.accept(selectedX, selectedY);  // קריאה ל-Consumer עם העמודות שנבחרו
                }
            }
            return null;
        });

        dialog.showAndWait();
    }


}
