package client.components.sharedSheets.loadFiles;

import client.components.sharedSheets.SheetsAndPermissionsManagerController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import okhttp3.*;
import java.io.File;
import java.io.IOException;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import static client.components.Utils.Constants.UPLOAD_FILE;
//import static jdk.internal.vm.vector.VectorSupport.test;

public class LoadSheetFilesController {

    private SheetsAndPermissionsManagerController parentController;
    @FXML
    private Button loadSheetFileButton;

    @FXML
    public void loadFileButtonClicked() throws Exception {

        File file = pickFile();
        uploadFile(file);
        //testSync();
        //test1Async();
        //testAsync(file);
    }

    private void testSync() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file","/C:/Users/noa40/OneDrive - The Academic College of Tel-Aviv Jaffa - MTA/שנה ב/קורסי בחירה/פיתוח תוכנה מבוסס גאווה/מטלות/שטיסל/shticell/engine/src/shticell/files/resources/grades.xml",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File("/C:/Users/noa40/OneDrive - The Academic College of Tel-Aviv Jaffa - MTA/שנה ב/קורסי בחירה/פיתוח תוכנה מבוסס גאווה/מטלות/שטיסל/shticell/engine/src/shticell/files/resources/grades.xml")))
                .build();
        Request request = new Request.Builder()
                .url(UPLOAD_FILE)
                .method("POST", body)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        System.out.println(response.body().string());
        //Response response = client.newCall(request).execute();
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

        System.out.println("New request is launched for: " + UPLOAD_FILE );

        //TODO - move to Utils the next line
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // טיפול בשגיאה במקרה של כשל
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // טיפול במקרה שהבקשה הצליחה
                    System.out.println(response.body().string());
                } else {
                    // טיפול במקרה שהבקשה לא הצליחה
                    System.out.println("Request failed: " + response.body().string());
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

    public void setParentController(SheetsAndPermissionsManagerController parentController) {
        this.parentController = parentController;
    }

}




