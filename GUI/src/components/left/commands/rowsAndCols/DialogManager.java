package components.left.commands.rowsAndCols;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.function.Consumer;

public class DialogManager {
    //    // דיאלוג להגדרת רוחב עמודות וגובה שורות
//    public void showColumnRowWidthDialog(int colIndex, int rowIndex, Consumer<Double> setColumnWidth, Consumer<Double> setRowHeight) {
//        Dialog<ButtonType> dialog = new Dialog<>();
//        dialog.setTitle("Set Column/Row Width");
//
//        GridPane dialogGrid = new GridPane();
//        dialogGrid.setHgap(10);
//        dialogGrid.setVgap(10);
//
//        TextField columnWidthField = new TextField();
//        columnWidthField.setPromptText("Column Width");
//        TextField rowHeightField = new TextField();
//        rowHeightField.setPromptText("Row Height");
//
//        dialogGrid.add(new Label("Column Width:"), 0, 0);
//        dialogGrid.add(columnWidthField, 1, 0);
//        dialogGrid.add(new Label("Row Height:"), 0, 1);
//        dialogGrid.add(rowHeightField, 1, 1);
//
//        dialog.getDialogPane().setContent(dialogGrid);
//        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//
//        dialog.setResultConverter(button -> {
//            if (button == ButtonType.OK) {
//                double columnWidth = Double.parseDouble(columnWidthField.getText());
//                double rowHeight = Double.parseDouble(rowHeightField.getText());
//
//                setColumnWidth.accept(columnWidth);  // קורא לפונקציה שעושה את העדכון
//                setRowHeight.accept(rowHeight);     // קורא לפונקציה שעושה את העדכון
//            }
//            return null;
//        });
//
//        dialog.showAndWait();
//    }
//
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
    public void showColumnWidthDialog( Consumer<Double> setColumnWidth) {
        showDialog(setColumnWidth,"Set Column Width","Column Width");
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
    public void showDialog(Consumer<Double> setColumnWidth,String DialogTitle,String DialogMessageInfo) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(DialogTitle);

        GridPane dialogGrid = new GridPane();
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);

        // Create a Spinner for column width with a range (e.g., 0.0 to 1000.0) and step value (e.g., 0.1)
        Spinner<Double> columnWidthSpinner = new Spinner<>(0.0, 1000.0, 100.0, 0.1);
        columnWidthSpinner.setEditable(true);  // Allow the user to type a value directly

        dialogGrid.add(new Label(DialogMessageInfo), 0, 0);
        dialogGrid.add(columnWidthSpinner, 1, 0);

        dialog.getDialogPane().setContent(dialogGrid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                double columnWidth = columnWidthSpinner.getValue();
                setColumnWidth.accept(columnWidth);  // Call the function to update the column width
            }
            return null;
        });

        dialog.showAndWait();
    }


    public void showRowHeightDialog( Consumer<Double> setRowHeight) {

        showDialog(setRowHeight,"Set Row Height","Row Height");
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

//    public void showColumnAlignmentDialog(int colIndex, Consumer<String> setColumnAlignment) {
//        Dialog<ButtonType> dialog = new Dialog<>();
//        dialog.setTitle("Set Column Alignment");
//
//        GridPane dialogGrid = new GridPane();
//        dialogGrid.setHgap(10);
//        dialogGrid.setVgap(10);
//
//        // שדה להזנת יישור
//        TextField alignmentField = new TextField();
//        alignmentField.setPromptText("Enter Alignment (LEFT, CENTER, RIGHT)");
//
//        dialogGrid.add(new Label("Column Alignment:"), 0, 0);
//        dialogGrid.add(alignmentField, 1, 0);
//
//        dialog.getDialogPane().setContent(dialogGrid);
//        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//
//        dialog.setResultConverter(button -> {
//            if (button == ButtonType.OK) {
//                String alignment = alignmentField.getText().toUpperCase();
//                setColumnAlignment.accept(alignment); // קריאה לפונקציה לעדכון היישור
//            }
//            return null;
//        });
//
//        dialog.showAndWait();
//    }

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
}

