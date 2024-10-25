package client.components.multiSheetsScreen.sheetsTable;

import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import client.components.Utils.http.HttpClientUtil;
import parts.SheetDetailsDTO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;

import static client.components.Utils.Constants.GET_SHEETS_LIST;
import static client.components.Utils.Constants.GSON_INSTANCE;

public class SheetsListRefresher extends TimerTask {

    private final Consumer<List<SheetDetailsDTO>> sheetsListConsumer;
    private int requestNumber;

    public SheetsListRefresher(Consumer<List<SheetDetailsDTO>> sheetsListConsumer) {
        this.sheetsListConsumer = sheetsListConsumer;
        requestNumber = 0;
    }

    @Override
    public void run() {

        HttpClientUtil.runAsyncByUrl(GET_SHEETS_LIST, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonListOfSheetDetails = response.body().string();

                Type type = new TypeToken<List<SheetDetailsDTO>>(){}.getType();
                List<SheetDetailsDTO> sheetDetailsDTO = GSON_INSTANCE.fromJson(jsonListOfSheetDetails, type);
                sheetsListConsumer.accept(sheetDetailsDTO);
            }
        });
    }
}
