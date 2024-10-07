package client.components.sheetManager.http;
import client.components.Utils.deserializer.CoordinateDeserializer;
import client.components.Utils.deserializer.EffectiveValueDeserializer;
import client.components.Utils.http.HttpClientUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import okhttp3.*;
import parts.SheetDTO;
import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;

import java.io.IOException;
import java.util.function.Consumer;

import static client.components.Utils.Constants.*;

public class RequestsManager {

    public void getSheetDTO(String sheetName, Consumer<SheetDTO> onSuccess, Consumer<String> onFailure) {
        //OkHttpClient client = new OkHttpClient().newBuilder().build();

        String finalUrl = HttpUrl
                .parse(GET_SHEET_DTO)
                .newBuilder()
                .addQueryParameter("sheetName", sheetName)
                .build()
                .toString();

        System.out.println("New request is launched for: " + GET_SHEET_DTO);

        HttpClientUtil.runAsyncByUrl(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // במקרה של כשל, נפעיל את ה-Consumer של onFailure
                Platform.runLater(() -> onFailure.accept("Error: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();

                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Coordinate.class, new CoordinateDeserializer())
                            .registerTypeAdapter(EffectiveValue.class, new EffectiveValueDeserializer())
                            .create();

                    // ממירים את ה-Response ל-SheetDTO
                    SheetDTO sheet = gson.fromJson(responseBody, SheetDTO.class);
                    System.out.println("Sheet constructor from json string: " + sheet);


                    // מעבירים את ה-DTO ל-UI באמצעות ה-Consumer של onSuccess
                    Platform.runLater(() -> onSuccess.accept(sheet));
                } else {
                    // במקרה של שגיאה נציג הודעה
                    Platform.runLater(() -> onFailure.accept("Error uploading file: " + responseBody));
                }
            }
        });
    }
    public void updateCell(String sheetName, String cellID, String newValue, Consumer<Boolean> onSuccess, Consumer<String> onFailure) {
        //public void updateCell(String sheetName, String coordinateStr, String newValue, Consumer<SheetDTO> onSuccess, Consumer<String> onFailure) {
        RequestBody formBody = new FormBody.Builder()
                .add("sheetName", sheetName)
                .add("cellID", cellID)
                .add("newValue", newValue) // הערך החדש לתא
                .build();

        // יצירת ה-Request עם URL של השרת וה-RequestBody
        Request request = new Request.Builder()
                .url(UPDATE_CELL) // כתובת ה-Servlet שלך
                .put(formBody) // הוספת ה-RequestBody לבקשה ב-HTTP PUT
                .build();

        // שליחת הבקשה באמצעות HttpClientUtil.runAsync
        HttpClientUtil.runAsyncByRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // טיפול במקרה של כשל
                Platform.runLater(() -> onFailure.accept("Error: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    // המרת התשובה ל-Boolean (true/false)
                    Boolean isUpdated = GSON_INSTANCE.fromJson(responseBody, Boolean.class);
                    Platform.runLater(() -> onSuccess.accept(isUpdated));
                } else {
                    Platform.runLater(() -> onFailure.accept(responseBody));
                }
            }
        });
    }
}
