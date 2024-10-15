package client.components.multiSheetsScreen.commands;

import client.components.Utils.http.HttpClientUtil;
import client.components.multiSheetsScreen.MultiSheetsScreenController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;


import javafx.scene.control.MenuItem;
import okhttp3.*;
import shticell.permissions.PermissionType;

import java.io.IOException;

import static client.components.Utils.Constants.REQUEST_PERMISSION_URL;

public class CommandsController {

    private MultiSheetsScreenController parentController;

    @FXML private Button handlePermissionRequest;
    @FXML private MenuButton requestPermissionMenuButton;
    @FXML private Button viewSheetButton;
    @FXML private Label sheetNameLabel;

    public void initializeCommandsController(SimpleBooleanProperty sheetSelected) {
        viewSheetButton.disableProperty().bind(sheetSelected.not());
        requestPermissionMenuButton.disableProperty().bind(sheetSelected.not());

        // יצירת פריטי MenuItem נוספים
        MenuItem readerOption = new MenuItem("READER");
        MenuItem writerOption = new MenuItem("WRITER");

        // הוספת פריטי ה-MenuItem ל-MenuButton
        requestPermissionMenuButton.getItems().addAll(readerOption, writerOption);

        setReqestPermissionOptionsActions(readerOption, writerOption);
    }

    public void setParentController(MultiSheetsScreenController multiSheetsScreenController) {
        this.parentController = multiSheetsScreenController;
    }

    public void handleViewSheetButtonClick(ActionEvent actionEvent) {
        parentController.switchToSheetManager();
    }

    public void setSheetNameLabel(String sheetName) { //TODO: maybe string property
        sheetNameLabel.setText(sheetName);
    }

    private void setReqestPermissionOptionsActions(MenuItem readerOption, MenuItem writerOption) {
//        readerOption.setOnAction(event -> System.out.println("READER option selected"));
//        writerOption.setOnAction(event -> System.out.println("WRITER option selected"));
        readerOption.setOnAction(event -> sendPermissionRequest(PermissionType.READER));
        writerOption.setOnAction(event -> sendPermissionRequest(PermissionType.WRITER));
    }

    private void sendPermissionRequest(PermissionType permissionType) {

       String sheetName = sheetNameLabel.getText();

       String body = "sheetName=" + sheetName + "\n" +
               "permissionType=" + permissionType;

       RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), body);

         Request request = new Request.Builder()
                .url(REQUEST_PERMISSION_URL)
                .method("PUT", requestBody)
                .addHeader("Content-Type", "text/plain")
                .build();
        //System.out.println("Sending permission request...");

        HttpClientUtil.runAsyncByRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Failed to send permission request: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful())
                {
                    System.out.println(responseBody);
                }
                else
                {
                    System.out.println("Error: " + responseBody);
                }
            }
        });

//        MediaType mediaType = MediaType.parse("text/plain");
//        RequestBody body = RequestBody.create(mediaType, "sheetName=beginner\r\npermissionType=READER");
//        Request request = new Request.Builder()
//                .url("http://localhost:8080/shticell/requestPermission")
//                .method("PUT", body)
//                .addHeader("Content-Type", "text/plain")
//                .build();
//        Response response = client.newCall(request).execute();
    }
}
