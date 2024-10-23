package client.components.mainAppController.updates;

import client.components.Utils.http.HttpClientUtil;
import client.components.mainAppController.Screen;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import shticell.permissions.RequestStatus;
import shticell.users.PermissionUpdate;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static client.components.Utils.Constants.*;

public class PermissionsUpdatesRefresher extends TimerTask {
    //private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<String> messageConsumer;
    private int requestNumber;
    private PermissionsUpdatesController parentController;


    public PermissionsUpdatesRefresher(Consumer<String> messageConsumer, PermissionsUpdatesController parentController) {

        this.messageConsumer = messageConsumer;
        this.parentController = parentController;
        //requestNumber = 0;
    }

    @Override
    public void run() {

//        if (!shouldUpdate.get()) {
//            return;
//        }

        //final int finalRequestNumber = ++requestNumber;
        //httpRequestLoggerConsumer.accept("About to invoke: " + GET_SHEETS_LIST + " | Users Request # " + finalRequestNumber);
        //System.out.println("About to invoke: " + GET_SHEETS_LIST + " | Users Request # " + finalRequestNumber);
        HttpClientUtil.runAsyncByUrl(GET_PERMISSION_UPDATE_FOR_USER, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...");
                //System.out.println("Users Request # " + finalRequestNumber + " | Ended with failure...");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonListOfSheetDetails = response.body().string();
                if (response.code() == 200) {
                    PermissionUpdate permissionUpdate = GSON_INSTANCE.fromJson(jsonListOfSheetDetails, PermissionUpdate.class);
                    String message = getUpdateMessageFromPermissionUpdate(permissionUpdate);
                    messageConsumer.accept(message);
                } else {
                    messageConsumer.accept("");
                }
            }
        });
    }

    public String getUpdateMessageFromPermissionUpdate(PermissionUpdate permissionUpdate) {

        if (permissionUpdate == null) {
            System.out.println("WARNING: permissionUpdate is null and status code is 200");
            return "";
        }
        RequestStatus requestStatus = permissionUpdate.getRequestStatus();
        Screen screen = parentController.getScreen();

        String message = "Update: permission: " + permissionUpdate.getPermission() + ", for sheet: " + permissionUpdate.getSheetName()
                + ", has been " + requestStatus.toString() + ". ";
        if (requestStatus == RequestStatus.APPROVED) {

            if (screen == Screen.MULTI_SHEETS) {
                message += "select the sheet again to see the changes.";
            } else if (screen == Screen.SINGLE_SHEET_MANAGER) {
                message += "go back to dashboard and select the sheet again to see the changes.";
            }
        }

        return message;
    }

}
