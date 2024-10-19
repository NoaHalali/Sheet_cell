package client.components.mainAppController.updates;

import client.components.Utils.http.HttpClientUtil;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import parts.SheetDetailsDTO;
import shticell.users.PermissionUpdate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static client.components.Utils.Constants.*;

public class PermissionsUpdatesRefresher extends TimerTask  {
    //private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<String> messageConsumer;
    private int requestNumber;
    //private final BooleanProperty shouldUpdate;

    public PermissionsUpdatesRefresher(Consumer<String> messageConsumer) {
        //this.shouldUpdate = shouldUpdate;
        //this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;
        this.messageConsumer = messageConsumer;
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
        String message ="Update: new permission received : " + permissionUpdate.getPermission() +", for sheet: " + permissionUpdate.getSheetName();
        return message;

    }

}
