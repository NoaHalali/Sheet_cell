package client.components.multiSheetsScreen.permissionsTable;

import client.components.Utils.http.HttpClientUtil;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import parts.permission.UserRequestDTO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

import static client.components.Utils.Constants.*;

public class RequestsListRefresher extends TimerTask {

    //private final Consumer<String> httpRequestLoggerConsumer;
    private final Consumer<List<UserRequestDTO>> requestsListConsumer;
    private int requestNumber;
    private String selectedSheet;
    //private final BooleanProperty shouldUpdate;


    public RequestsListRefresher(Consumer<List<UserRequestDTO>> requestsListConsumer,String selectedSheet) {
        //this.shouldUpdate = shouldUpdate;
        //this.httpRequestLoggerConsumer = httpRequestLoggerConsumer;

        this.requestsListConsumer = requestsListConsumer;
        this.selectedSheet = selectedSheet;
        requestNumber = 0;
    }

    @Override
    public void run() {

        final int finalRequestNumber = ++requestNumber;

        String finalUrl = HttpUrl
                .parse(GET_REQUESTS_LIST_URL)
                .newBuilder()
                .addQueryParameter("sheetName", selectedSheet)
                .build()
                .toString();

        //httpRequestLoggerConsumer.accept("About to invoke: " + GET_SHEETS_LIST + " | Users Request # " + finalRequestNumber);
        //System.out.println("About to invoke: " + GET_SHEETS_LIST + " | Users Request # " + finalRequestNumber);
        HttpClientUtil.runAsyncByUrl(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Ended with failure...");
                System.out.println("Users Request # " + finalRequestNumber + " | Ended with failure...");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonListOfRequests = response.body().string();
                // httpRequestLoggerConsumer.accept("Users Request # " + finalRequestNumber + " | Response: " + jsonArrayOfUsersNames);
                // System.out.println("Users Request # " + finalRequestNumber + " | Response: " + jsonListOfRequests);
                Type type = new TypeToken<List<UserRequestDTO>>(){}.getType();
                List<UserRequestDTO> userRequestsDTO = GSON_INSTANCE.fromJson(jsonListOfRequests, type);
                requestsListConsumer.accept(userRequestsDTO);
            }
        });
    }
}
