package client.components.multiSheetsScreen.loadFiles;

import client.components.Utils.StageUtils;
import client.components.Utils.http.HttpClientUtil;
import client.components.multiSheetsScreen.MultiSheetsScreenController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import okhttp3.*;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;

import static client.components.Utils.Constants.UPLOAD_FILE;
//import static jdk.internal.vm.vector.VectorSupport.test;

public class LoadSheetFilesController {

    private MultiSheetsScreenController parentController;
    @FXML
    private Button loadSheetFileButton;

    @FXML
    public void loadFileButtonClicked() throws Exception {

        File file = pickFile();
        if (file != null) {
            uploadFile(file);
        }
    }

    private void uploadFile(File file) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("text/plain")))
                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
                        .build();

        Request request = new Request.Builder()
                .url(UPLOAD_FILE)
                .method("POST", body)
                .build();



        HttpClientUtil.runAsyncByRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // טיפול בשגיאה במקרה של כשל
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        // טיפול במקרה שהבקשה הצליחה
                        System.out.println("Upload successful: " + responseBody);
                        //parentController.switchToSheetManager();
                        //parentController.switchToMultiSheetsScreen();
                    });

                } else {
                    Platform.runLater(() ->
                            StageUtils.showAlert("Error", "Error uploading file" + responseBody)
                    );
                }
            }
        });
    }

    public File pickFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select shticell file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File file = parentController.showFileChooser(fileChooser);
        return file;
    }

    public void setParentController(MultiSheetsScreenController parentController) {
        this.parentController = parentController;
    }

}




