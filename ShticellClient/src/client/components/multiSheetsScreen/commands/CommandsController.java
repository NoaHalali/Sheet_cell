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
import shticell.permissions.RequestStatus;

import java.io.IOException;

import static client.components.Utils.Constants.HANDLE_REQUEST_PERMISSION_URL;
import static client.components.Utils.Constants.REQUEST_PERMISSION_URL;

public class CommandsController {

    private MultiSheetsScreenController parentController;

    @FXML private Button viewSheetButton;
    @FXML private Label sheetNameLabel;
    @FXML private MenuButton requestPermissionMenuButton;
    @FXML private MenuButton handleRequestMenuButton;


    public void initializeCommandsController(SimpleBooleanProperty sheetSelected,SimpleBooleanProperty pendingRequestSelected) {
        viewSheetButton.disableProperty().bind(sheetSelected.not());
        requestPermissionMenuButton.disableProperty().bind(sheetSelected.not());
        handleRequestMenuButton.disableProperty().bind(pendingRequestSelected.not());

        setRequestPermissionMenuItems();
        setHandleRequestMenuItems();
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

    private void setRequestPermissionMenuItems() {
        // יצירת פריטי MenuItem נוספים
        MenuItem readerOption = new MenuItem("READER");
        MenuItem writerOption = new MenuItem("WRITER");

        // הוספת פריטי ה-MenuItem ל-MenuButton
        requestPermissionMenuButton.getItems().addAll(readerOption, writerOption);
        setReqestPermissionOptionsActions(readerOption, writerOption);
    }

    private void setHandleRequestMenuItems() {
        // יצירת פריטי MenuItem נוספים
        MenuItem acceptOption = new MenuItem("APPORVE");
        MenuItem denyOption = new MenuItem("REJECT");

        // הוספת פריטי ה-MenuItem ל-MenuButton
        handleRequestMenuButton.getItems().addAll(acceptOption, denyOption);
        setAcceptOrDenyRequesActions(acceptOption, denyOption);
    }

    private void setReqestPermissionOptionsActions(MenuItem readerOption, MenuItem writerOption) {
        readerOption.setOnAction(event -> sendPermissionRequest(PermissionType.READER));
        writerOption.setOnAction(event -> sendPermissionRequest(PermissionType.WRITER));
    }

    private void setAcceptOrDenyRequesActions(MenuItem readerOption, MenuItem writerOption) {
        readerOption.setOnAction(event -> handlePermissionRequest(RequestStatus.APPROVED));
        writerOption.setOnAction(event -> handlePermissionRequest(RequestStatus.REJECTED));
    }

    private void sendPermissionRequest(PermissionType permissionType) {

        String sheetName = parentController.getSelectedSheetName();
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
    }

    private void handlePermissionRequest(RequestStatus requestStatus) {

        String sheetName = parentController.getSelectedSheetName();
        int requestNumber = parentController.getSelectedRequestNumber();

        String body = "sheetName=" + sheetName + "\n" +
                "requestNumber=" + requestNumber + "\n" +
                "requestStatus=" + requestStatus;

        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), body);

        Request request = new Request.Builder()
                .url(HANDLE_REQUEST_PERMISSION_URL)
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
    }
}
