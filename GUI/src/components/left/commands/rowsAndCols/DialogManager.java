package components.left.commands.rowsAndCols;

import components.Utils.StageUtils;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.function.Consumer;

public class DialogManager {

    //    // דיאלוג להגדרת יישור עמודות
//    public void showColumnAlignmentDialog(int colIndex, Consumer<String> setColumnAlignment) {
//        Dialog<ButtonType> dialog = new Dialog<>();
//        dialog.setTitle("Set Column Alignment");
//
//        TextField alignmentField = new TextField();
//        alignmentField.setPromptText("Enter Alignment (LEFT, CENTER, RIGHT)");
//
//        dialog.getDialogPane().setContent(alignmentField);
//        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//
//        dialog.setResultConverter(button -> {
//            if (button == ButtonType.OK) {
//                String alignment = alignmentField.getText().toUpperCase();
//                setColumnAlignment.accept(alignment); // קורא לפונקציה שמיישמת את היישור
//            }
//            return null;
//        });
//
//        dialog.showAndWait();
//    }
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
//        Dialog<ButtonType> dialog = new Dialog<>();
//        dialog.setTitle("Set Row Height");
//
//        GridPane dialogGrid = new GridPane();
//        dialogGrid.setHgap(10);
//        dialogGrid.setVgap(10);
//
//        TextField rowHeightField = new TextField();
//        rowHeightField.setPromptText("Row Height");
//
//        dialogGrid.add(new Label("Row Height:"), 0, 0);
//        dialogGrid.add(rowHeightField, 1, 0);
//
//        dialog.getDialogPane().setContent(dialogGrid);
//        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//
//        dialog.setResultConverter(button -> {
//            if (button == ButtonType.OK) {
//                double rowHeight = Double.parseDouble(rowHeightField.getText());
//                setRowHeight.accept(rowHeight);  // קריאה לפונקציה לעדכון גובה שורה
//            }
//            return null;
//        });
//
//        dialog.showAndWait();
    }

    public void showDialog(int currentValue, Consumer<Integer> setColumnWidth, String dialogTitle, String dialogMessageInfo) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);

        GridPane dialogGrid = new GridPane();
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);

        int minValue = 1;
        int maxValue = 100;
        int initialValue = currentValue;
        int step = 1;
        Spinner<Integer> spinner = createSpinner(minValue, maxValue, initialValue, step);
        // Label עבור הודעת שגיאה
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");  // הגדרת צבע הטקסט לאדום, בהתחלה מוסתר

        dialogGrid.add(new Label(dialogMessageInfo), 0, 0);
        dialogGrid.add(spinner, 1, 0);
        dialogGrid.add(errorLabel, 0, 2, 2, 1);  // להוסיף את הודעת השגיאה לרשת, שורה 2

        dialog.getDialogPane().setContent(dialogGrid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextFormatter<Integer> textFormatter = createIntegerTextFormatter(minValue, errorLabel);

        // קישור ה-TextFormatter ל-TextField של ה-Spinner
        spinner.getEditor().setTextFormatter(textFormatter);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                int columnWidth = spinner.getValue();
                setColumnWidth.accept(columnWidth);  // קריאה לפונקציה לעדכן את רוחב העמודה
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

    private Spinner<Integer> createSpinner(int minValue, int maxValue, int initialValue, int step) {
        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue, maxValue, initialValue, step);
        spinner.setValueFactory(valueFactory);
        spinner.setEditable(true);  // Allow user to enter value manually
        return spinner;
    }

    private TextFormatter<Integer> createIntegerTextFormatter(int minValue, Label errorLabel) {
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

}
