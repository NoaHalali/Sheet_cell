package client.components.sheetManager.parts.top.updates;

import client.components.Utils.http.HttpClientUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import shticell.users.PermissionUpdate;

import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static client.components.Utils.Constants.GET_CHANGED_PERMISSION_FOR_USER;
import static client.components.Utils.Constants.GSON_INSTANCE;

public class SheetUpdatesRefresher extends TimerTask  {
    //private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<String> messageConsumer;
    private int requestNumber;
    //private final BooleanProperty shouldUpdate;

    public SheetUpdatesRefresher(Consumer<String> messageConsumer) {
        //this.shouldUpdate = shouldUpdate;
        //this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
        this.messageConsumer = messageConsumer;
        //requestNumber = 0;
    }

    @Override
    public void run() {

        HttpClientUtil.runAsyncByUrl(GET_CHANGED_PERMISSION_FOR_USER, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...");
                //System.out.println("Users Request # " + finalRequestNumber + " | Ended with failure...");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonListOfSheetDetails = response.body().string();
                if(response.code()==200) {
                    PermissionUpdate permissionUpdate = GSON_INSTANCE.fromJson(jsonListOfSheetDetails, PermissionUpdate.class);
                    String message =getUpdateMessageFromPermissionUpdate(permissionUpdate);
                    messageConsumer.accept(message);
                }else {
                    messageConsumer.accept("");
                }
            }
        });
    }
    public String getUpdateMessageFromPermissionUpdate(PermissionUpdate permissionUpdate) {

        String message ="Update: permission: " + permissionUpdate.getPermission() +", for sheet: " + permissionUpdate.getSheetName()
                + ", has been changed to: " + permissionUpdate.getRequestStatus();
        return message;

    }
}
