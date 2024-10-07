package client.components.sheetManager.parts.top.fileChooser;

import client.components.Utils.StageUtils;
import client.components.sheetManager.SheetManagerController;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;

import java.io.File;

public class FileChooserController {

    private SheetManagerController mainController;

    @FXML private Label filePathLabel;
    @FXML private Button loadFileButton;
    @FXML private ProgressBar progressBar; // הוספת ProgressIndicator

    public void setMainController(SheetManagerController mainController) {
        this.mainController = mainController;
    }

    @FXML public void openFileButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select shticell file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = mainController.showFileChooser(fileChooser);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        filePathLabel.setText(absolutePath);

        // יצירת משימה לטעינת הקובץ ברקע
        Task<Void> loadFileTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateProgress(0, 100); // התחלת התקדמות ב-0%

                // קריאת נתוני הקובץ
                mainController.loadFileToSystem(absolutePath);

                // ניתן להוסיף השהיה קטנה כדי לסמלץ התקדמות
                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(5); // השהיה קצרה לדימוי התקדמות
                    updateProgress(i, 100); // עדכון התקדמות
                }

                updateProgress(100, 100); // סיום התקדמות
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                // עדכון ה-UI במקרה של הצלחה

                mainController.initializeComponentsAfterLoad(); // קריאה למתודה שמעדכנת את כל הרכיבים

                progressBar.setVisible(false); // הסתרת ProgressIndicator
            }

            @Override
            protected void failed() {
                super.failed();
                // טיפול בשגיאה במקרה של כשל בטעינת הקובץ
                Throwable exception = getException();
                if (exception != null) {
                    StageUtils.showAlert("Error", exception.getMessage());
                } else {
                    StageUtils.showAlert("Error", "Unknown error occurred.");
                }
                progressBar.setVisible(false); // הסתרת ProgressIndicator
            }
//            @Override
//            protected void failed() {
//
//
//                super.failed();
//                // טיפול בשגיאה במקרה של כשל בטעינת הקובץ
//                //System.out.println("Failed to load file: " + getException().getMessage());
//                StageUtils.showAlert("Error", getException().getMessage());
//                progressBar.setVisible(false); // הסתרת ProgressIndicator
//            }

        };

        // קישור ה-ProgressIndicator להתקדמות המשימה
        progressBar.progressProperty().bind(loadFileTask.progressProperty());
        progressBar.setVisible(true);  // הצגת ה-ProgressIndicator

        // הפעלת המשימה ב-Thread נפרד
        new Thread(loadFileTask).start();
    }
}
