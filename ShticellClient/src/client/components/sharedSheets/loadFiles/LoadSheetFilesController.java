package client.components.sharedSheets.loadFiles;

import client.components.sharedSheets.SheetsAndPermissionsManagerController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import okhttp3.*;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import static client.components.Utils.Constants.UPLOAD_FILE;

public class LoadSheetFilesController {

    private SheetsAndPermissionsManagerController parentController;
    @FXML
    private Button loadSheetFileButton;

    @FXML
    public void loadFileButtonClicked() throws IOException {
     File file =pickFile();
         uploadFile(file);
    }
    public File pickFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select shticell file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File file = parentController.showFileChooser(fileChooser);
        return file;
    }
    public void uploadFile(File file) {
        if (file != null) {
            OkHttpClient client = new OkHttpClient();

            // יצירת גוף הבקשה עם הקובץ שנבחר
            RequestBody fileBody = RequestBody.create(file, MediaType.parse("application/xml"));


            // יצירת בקשה מרובת חלקים (multipart) עם הקובץ
            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(), fileBody)
                    .build();

            // בניית בקשת POST לשרת
            Request request = new Request.Builder()
                    .url(UPLOAD_FILE)
                    .post(requestBody)
                    .build();

            // שליחת הבקשה לשרת בצורה אסינכרונית
            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() ->
                            System.out.println("File upload failed: " + e.getMessage())
                    );
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        Platform.runLater(() ->
                                System.out.println("File uploaded successfully: " + responseBody)
                        );
                    } else {
                        Platform.runLater(() ->
                                System.out.println("File upload failed: " + response.code())
                        );
                    }
                }
            });
        } else {
            System.out.println("No file selected.");
        }
    }

    public void setParentController(SheetsAndPermissionsManagerController parentController) {
        this.parentController = parentController;
    }

}




