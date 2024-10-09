package client.components.sheetManager.http;
import client.components.Utils.StageUtils;
import client.components.Utils.deserializer.CoordinateDeserializer;
import client.components.Utils.deserializer.EffectiveValueDeserializer;
import client.components.Utils.http.HttpClientUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import okhttp3.*;
import parts.SheetDTO;
import parts.cell.CellDTO;
import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;

import static client.components.Utils.Constants.*;

public class RequestsManager {

    private final String sheetName;

    public RequestsManager(String sheetName) {
        this.sheetName = sheetName;
    }
    public void getSheetDTO(Consumer<SheetDTO> onSuccess, Consumer<String> onFailure) {
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

    public void updateCell(String cellID, String newValue, Consumer<Boolean> onSuccess, Consumer<String> onFailure) {
        // יצירת ה-URL עם הפרמטרים (Query Parameters)
        String finalUrl = HttpUrl.parse(UPDATE_CELL)
                .newBuilder()
                .addQueryParameter("sheetName", sheetName)  // הוספת שם הגיליון
                .addQueryParameter("cellID", cellID)        // הוספת ID של התא
                .addQueryParameter("newValue", newValue)    // הוספת הערך החדש
                .build()
                .toString();

        // יצירת הבקשה
        Request request = new Request.Builder()
                .url(finalUrl) // ה-URL עם הפרמטרים
                .put(RequestBody.create("", null)) // HTTP PUT עם גוף ריק, מכיוון שאנחנו שולחים את הכל ב-Query Parameters
                .build();

        // שליחת הבקשה באמצעות HttpClientUtil.runAsync
        HttpClientUtil.runAsyncByRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> onFailure.accept("Error: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    Boolean isUpdated = GSON_INSTANCE.fromJson(responseBody, Boolean.class);
                    Platform.runLater(() -> onSuccess.accept(isUpdated));
                } else {
                    Platform.runLater(() -> onFailure.accept(responseBody));
                }
            }
        });
    }


    public void getCellDTO(String cellID, Consumer<CellDTO> onSuccess, Consumer<String> onFailure) {
        String finalUrl = HttpUrl.parse(GET_CELL_DTO_URL)
                .newBuilder()
                .addQueryParameter("sheetName", sheetName)
                .addQueryParameter("cellID", cellID)
                .build()
                .toString();

        HttpClientUtil.runAsyncByUrl(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> onFailure.accept("Error: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();

                if (response.isSuccessful()) {
                    // שימוש ב-GsonBuilder עם ה-TypeAdapterים המותאמים
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Coordinate.class, new CoordinateDeserializer())
                            .registerTypeAdapter(EffectiveValue.class, new EffectiveValueDeserializer())
                            .create();

                    // המרת ה-JSON חזרה ל-CellDTO
                    CellDTO cellDTO = gson.fromJson(responseBody, CellDTO.class);
                    Platform.runLater(() -> onSuccess.accept(cellDTO));
                } else {
                    Platform.runLater(() -> onFailure.accept(responseBody));
                }
            }
        });
    }

    public void getSheetDtoByVersion(String version, Consumer<SheetDTO> onSuccess, Consumer<String> onFailure) {

        String finalUrl = HttpUrl
                .parse(GET_SHEET_DTO_BY_VERSION)
                .newBuilder()
                .addQueryParameter("sheetName", sheetName)
                .addQueryParameter("version" ,version)
                .build()
                .toString();

        System.out.println("New request is launched for: " + GET_SHEET_DTO_BY_VERSION);

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
    }//get

    public void addRange(String rangeName, String rangeDefinition,Consumer<List<String>> onSuccess,Consumer<String> onFailure) throws Exception {
        String RESOURCE = "/api/add-range"; // שינוי ה-RESOURCE בהתאם לצורך שלך

        // יצירת ה-body בצורה של טקסט רגיל עם מפריד שורות "\n"
        String body = "sheetName=" + sheetName + "\n" +
                "rangeName=" + rangeName + "\n" +
                "rangeDefinition=" + rangeDefinition;

        // יצירת בקשת POST עם ה-body
        Request request = new Request.Builder()
                .url(ADD_RANGE_URL) // ה-URL של הבקשה
                .post(RequestBody.create(body.getBytes())) // שליחת ה-body בפורמט טקסט
                .build();

        HttpClientUtil.runAsyncByRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    StageUtils.showAlert("Error:", "Failed to add range: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    Type listOfStringType = new TypeToken<List<String>>() {}.getType();
                    List<String> rangeNames = GSON_INSTANCE.fromJson(responseBody, listOfStringType);
                    Platform.runLater(() -> onSuccess.accept(rangeNames));
                } else {
                    Platform.runLater(() -> onFailure.accept("Error uploading file: " + responseBody));
                }
            }
        });
    }

    public void deleteRange(String rangeName, Consumer<List<String>> onSuccess, Consumer<String> onFailure) {
        String finalUrl = HttpUrl.parse(DELETE_RANGE_URL)
                .newBuilder()
                .addQueryParameter("sheetName", sheetName)
                .addQueryParameter("rangeName", rangeName)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .delete() // בקשת DELETE
                .build();

        HttpClientUtil.runAsyncByRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    StageUtils.showAlert("Error:", "Failed to delete range: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();

                if (response.isSuccessful()) {
                    Type listOfStringType = new TypeToken<List<String>>() {}.getType();
                    List<String> rangeNames = GSON_INSTANCE.fromJson(responseBody, listOfStringType);
                    Platform.runLater(() -> onSuccess.accept(rangeNames));
                } else {
                    Platform.runLater(() -> onFailure.accept("Error uploading file: " + responseBody));
                }
            }
        });
    }


    public void getRangeCoordinates( String rangeName,Consumer<List<Coordinate>> onSuccess,Consumer<String> onFailure){
        String finalUrl = HttpUrl
                .parse(GET_RANGE_COORDINATES)
                .newBuilder()
                .addQueryParameter("sheetName", sheetName)
                .addQueryParameter("rangeName" ,rangeName)
                .build()
                .toString();

        System.out.println("New request is launched for: " + GET_RANGE_COORDINATES);

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
                            .create();

                    Type listOfCoordinateType = new TypeToken<List<Coordinate>>() {}.getType();
                        List<Coordinate> rangeCoordinates = gson.fromJson(responseBody, listOfCoordinateType);



                    // מעבירים את ה-DTO ל-UI באמצעות ה-Consumer של onSuccess
                    Platform.runLater(() -> onSuccess.accept(rangeCoordinates));
                } else {
                    // במקרה של שגיאה נציג הודעה
                    Platform.runLater(() -> onFailure.accept("Error uploading file: " + responseBody));
                }
            }
        });
    }
//    public void getRangeNames(Consumer<List<String>> onSuccess, Consumer<String> onFailure){
//        String finalUrl = HttpUrl.parse(GET_CELL_DTO_URL)
//                .newBuilder()
//                .addQueryParameter("sheetName", sheetName)
//                .build()
//                .toString();
//
//        HttpClientUtil.runAsyncByUrl(finalUrl, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Platform.runLater(() -> onFailure.accept("Error: " + e.getMessage()));
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseBody = response.body().string();
//
//                if (response.isSuccessful()) {
//                    Type listOfStringType = new TypeToken<List<String>>() {}.getType();
//                    List<String> rangeNames = GSON_INSTANCE.fromJson(responseBody, listOfStringType);
//                    Platform.runLater(() -> onSuccess.accept(rangeNames));
//
//                } else {
//                    Platform.runLater(() -> onFailure.accept(responseBody));
//                }
//            }
//        });
//    }
}
