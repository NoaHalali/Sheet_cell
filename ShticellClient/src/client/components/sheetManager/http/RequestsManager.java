package client.components.sheetManager.http;
import client.components.Utils.StageUtils;
import client.components.Utils.deserializer.CoordinateDeserializer;
import client.components.Utils.deserializer.EffectiveValueDeserializer;
import client.components.Utils.http.EffectiveValueSerializer;
import client.components.Utils.http.HttpClientUtil;
import client.components.sheetManager.SheetManagerController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import okhttp3.*;
import parts.SheetDTO;
import parts.cell.CellDTO;
import shticell.permissions.PermissionType;
import shticell.sheets.sheet.Sheet;
import shticell.sheets.sheet.parts.cell.coordinate.Coordinate;
import shticell.sheets.sheet.parts.cell.expression.effectiveValue.EffectiveValue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static client.components.Utils.Constants.*;

public class RequestsManager {

    private String sheetName;
    private PermissionType permissionType;
    //private final SheetManagerController parentController;

    public RequestsManager(String sheetName, PermissionType permissionType) {
        this.sheetName = sheetName;
        this.permissionType = permissionType;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }

    public void getSheetDTO(Consumer<SheetDTO> onSuccess, Consumer<String> onFailure) {
        //OkHttpClient client = new OkHttpClient().newBuilder().build();
        String finalUrl = HttpUrl
                .parse(GET_SHEET_DTO)
                .newBuilder()
                .addQueryParameter("sheetName", sheetName)
                .addQueryParameter("permissionType", permissionType.toString())
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
                    Platform.runLater(() -> onFailure.accept(responseBody));
                }
            }
        });
    }

    public void updateCell(String cellID, String newValue, Consumer<Boolean> onSuccess, Consumer<String> onFailure) {
        // יצירת ה-URL עם הפרמטרים (Query Parameters)
        String finalUrl = HttpUrl.parse(UPDATE_CELL)
                .newBuilder()
                //.addQueryParameter("sheetName", sheetName)  // הוספת שם הגיליון
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
                   // String errorMessage = GSON_INSTANCE.fromJson(responseBody, Map.class).get("error").toString();
                    String errorMessage = GSON_INSTANCE.fromJson(responseBody, String.class);
                    Platform.runLater(() -> onFailure.accept(errorMessage));

                }
            }
        });
    }

    public void getCellDTO(String cellID, Consumer<CellDTO> onSuccess, Consumer<String> onFailure) {
        String finalUrl = HttpUrl.parse(GET_CELL_DTO_URL)
                .newBuilder()
                //.addQueryParameter("sheetName", sheetName)
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
                    String errorMessage = GSON_INSTANCE.fromJson(responseBody, Map.class).get("error").toString();
                    Platform.runLater(() -> onFailure.accept(errorMessage));
                }
            }
        });
    }

    public void getSheetDtoByVersion(String version, Consumer<SheetDTO> onSuccess, Consumer<String> onFailure) {

        String finalUrl = HttpUrl
                .parse(GET_SHEET_DTO_BY_VERSION)
                .newBuilder()
                //.addQueryParameter("sheetName", sheetName)
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
                    String errorMessage = GSON_INSTANCE.fromJson(responseBody, Map.class).get("error").toString();
                    Platform.runLater(() -> onFailure.accept(errorMessage));
                }
            }
        });
    }//get

    public void addRange(String rangeName, String rangeDefinition,Consumer<List<String>> onSuccess,Consumer<String> onFailure) throws Exception {
        String RESOURCE = "/api/add-range"; // שינוי ה-RESOURCE בהתאם לצורך שלך

        // יצירת ה-body בצורה של טקסט רגיל עם מפריד שורות "\n"
        String body = "rangeName=" + rangeName + "\n" +
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
                    String errorMessage = GSON_INSTANCE.fromJson(responseBody, Map.class).get("error").toString();
                    Platform.runLater(() -> onFailure.accept(errorMessage));
                }
            }
        });
    }

    public void deleteRange(String rangeName, Consumer<List<String>> onSuccess, Consumer<String> onFailure) {
        String finalUrl = HttpUrl.parse(DELETE_RANGE_URL)
                .newBuilder()
                //.addQueryParameter("sheetName", sheetName)
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
                    String errorMessage = GSON_INSTANCE.fromJson(responseBody, Map.class).get("error").toString();
                    Platform.runLater(() -> onFailure.accept(errorMessage));
                }
            }
        });
    }


    public void getRangeCoordinates(String rangeName,Consumer<List<Coordinate>> onSuccess,Consumer<String> onFailure){
        String finalUrl = HttpUrl
                .parse(GET_RANGE_COORDINATES)
                .newBuilder()
                //.addQueryParameter("sheetName", sheetName)
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
                    String errorMessage = GSON_INSTANCE.fromJson(responseBody, Map.class).get("error").toString();
                    Platform.runLater(() -> onFailure.accept(errorMessage));
                }
            }
        });
    }

    public void getColumnDataInRange(String rangeDefinition, Consumer<List<CellDTO>> onSuccess, Consumer<String> onFailure) {
        // יצירת URL עם הפרמטרים
        String finalUrl = HttpUrl.parse(GET_COLUMN_DATA_IN_RANGE)
                .newBuilder()
                //.addQueryParameter("sheetName", sheetName)
                .addQueryParameter("rangeDefinition", rangeDefinition)
                .build()
                .toString();

        // יצירת בקשת GET
        Request request = new Request.Builder()
                .url(finalUrl)
                .get()
                .build();

        // קריאה אסינכרונית לשרת
        HttpClientUtil.runAsyncByUrl(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> onFailure.accept("Failed to fetch column data: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    // המרת ה-JSON חזרה לרשימת CellDTO
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Coordinate.class, new CoordinateDeserializer())
                            .registerTypeAdapter(EffectiveValue.class, new EffectiveValueDeserializer())
                            .create();
                    List<CellDTO> columnData = gson.fromJson(responseBody, new TypeToken<List<CellDTO>>(){}.getType());
                    Platform.runLater(() -> onSuccess.accept(columnData));
                } else {
                    String errorMessage = GSON_INSTANCE.fromJson(responseBody, Map.class).get("error").toString();
                    Platform.runLater(() -> onFailure.accept(errorMessage));
                }
            }
        });
    }

    public void getSortedSheetDTO(String rangeDefinition, List<Character> columnsToSortBy, Consumer<SheetDTO> onSuccess, Consumer<String> onFailure) {
        // יצירת URL עם פרמטרים מתאימים ל-batch של ה-GET
        HttpUrl.Builder urlBuilder = HttpUrl.parse(GET_SORTED_SHEET_DTO)
                .newBuilder()
                //.addQueryParameter("sheetName", sheetName)
                .addQueryParameter("rangeDefinition", rangeDefinition);

        // המרת רשימת העמודות לרשימה ב-query parameter
        for (Character column : columnsToSortBy) {
            urlBuilder.addQueryParameter("columnsToSortBy", column.toString());
        }

        String finalUrl = urlBuilder.build().toString();

        // יצירת בקשת GET
        Request request = new Request.Builder()
                .url(finalUrl)
                .get()
                .build();

        // קריאה אסינכרונית לשרת
        HttpClientUtil.runAsyncByRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> onFailure.accept("Failed to get sorted sheetDTO: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    // המרת ה-JSON ל-SheetDTO
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Coordinate.class, new CoordinateDeserializer())
                            .registerTypeAdapter(EffectiveValue.class, new EffectiveValueDeserializer())
                            .create();
                    SheetDTO sortedSheet = gson.fromJson(responseBody, SheetDTO.class);
                    Platform.runLater(() -> onSuccess.accept(sortedSheet));
                } else {
                    String errorMessage = GSON_INSTANCE.fromJson(responseBody, Map.class).get("error").toString();
                    Platform.runLater(() -> onFailure.accept(errorMessage));
                }
            }
        });
    }

//    public void getClonedSheet(Consumer<Sheet> onSuccess, Consumer<String> onFailure) {
//        //OkHttpClient client = new OkHttpClient().newBuilder().build();
//
//        String finalUrl = HttpUrl
//                .parse(GET_CLONED_SHEET)
//                .newBuilder()
//                .addQueryParameter("sheetName", sheetName)
//                .build()
//                .toString();
//
//        System.out.println("New request is launched for: " + GET_CLONED_SHEET);
//
//        HttpClientUtil.runAsyncByUrl(finalUrl, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                // במקרה של כשל, נפעיל את ה-Consumer של onFailure
//                Platform.runLater(() -> onFailure.accept("Error: " + e.getMessage()));
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseBody = response.body().string();
//
//                if (response.isSuccessful()) {
//                    Gson gson = new GsonBuilder()
//                            .registerTypeAdapter(Coordinate.class, new CoordinateDeserializer())
//                            .registerTypeAdapter(EffectiveValue.class, new EffectiveValueDeserializer())
//                            .create();
//
//                    // ממירים את ה-Response ל-SheetDTO
//                    Sheet sheet = gson.fromJson(responseBody, Sheet.class);
//                    System.out.println("Sheet constructor from json string: " + sheet);
//
//
//                    // מעבירים את ה-DTO ל-UI באמצעות ה-Consumer של onSuccess
//                    Platform.runLater(() -> onSuccess.accept(sheet));
//                } else {
//                    // במקרה של שגיאה נציג הודעה
//                    Platform.runLater(() -> onFailure.accept("Error uploading file: " + responseBody));
//                }
//            }
//        });
//    }

    public void setEngineInWhatIfMode(Coordinate coord, Consumer<Void> onSuccess, Consumer<String> onFailure) {
        String body = "cellID=" + coord.toString() + "\n";

        Request request = new Request.Builder()
                .url(SET_SHEET_IN_WHAT_IF_MODE)
                .put(RequestBody.create(body.getBytes()))
                .build();

        HttpClientUtil.runAsyncByRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> onFailure.accept("Failed to activate what if mode: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Platform.runLater(() -> onSuccess.accept(null));
                } else {
                    String errorMessage = "Unknown error";
                    if (response.body() != null) {
                        errorMessage = response.body().string();
                    }
                    final String finalErrorMessage = errorMessage;
                    Platform.runLater(() -> onFailure.accept("Error: " + finalErrorMessage));
                }
                }

        });
    }

    public void calculateWhatIfValueForCell(double value,Consumer<SheetDTO> onSuccess, Consumer<String> onFailure){
        // יצירת ה-body בצורה של טקסט רגיל עם מפריד שורות "\n"
        String body = "value=" + value + "\n" ;

        // יצירת בקשת POST עם ה-body
        Request request = new Request.Builder()
                .url(CALCULATE_WHAT_IF_VALUE_FOR_CELL) // ה-URL של הבקשה
                .put(RequestBody.create(body.getBytes())) // שליחת ה-body בפורמט טקסט
                .build();

        HttpClientUtil.runAsyncByRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    StageUtils.showAlert("Error:", "Failed to change cell value " + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Coordinate.class, new CoordinateDeserializer())
                            .registerTypeAdapter(EffectiveValue.class, new EffectiveValueDeserializer())
                            .create();
                    SheetDTO changeSheet = gson.fromJson(responseBody, SheetDTO.class);
                    onSuccess.accept(changeSheet);
                } else {
                    String errorMessage = GSON_INSTANCE.fromJson(responseBody, Map.class).get("error").toString();
                    Platform.runLater(() -> onFailure.accept("HIIIII"+errorMessage)); //TODO: fix printing and delete the HIIII
                }
            }
        });
    }

    public void getDistinctValuesOfMultipleColsInRange(List<Character> cols, String rangeDefinition, Consumer<Map<String, Set<EffectiveValue>>> onSuccess, Consumer<String> onFailure) {
        // המרת רשימת העמודות לפורמט שניתן לשלוח ב-Query Parameters
        HttpUrl.Builder urlBuilder = HttpUrl.parse(GET_DISTINCT_VALUES_OF_MULTIPLE_COLS_PATH)
                .newBuilder()
                //.addQueryParameter("sheetName", sheetName)
                .addQueryParameter("rangeDefinition", rangeDefinition);

        // הוספת העמודות כ-Query Parameters
        for (Character col : cols) {
            urlBuilder.addQueryParameter("cols", col.toString());
        }

        String finalUrl = urlBuilder.build().toString();

        // יצירת בקשת GET
        Request request = new Request.Builder()
                .url(finalUrl)
                .get()
                .build();

        // קריאה אסינכרונית לשרת
        HttpClientUtil.runAsyncByRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> onFailure.accept("Failed to fetch distinct values: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    // שימוש ב-Gson עם EffectiveValueDeserializer
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(EffectiveValue.class, new EffectiveValueDeserializer())  // השימוש ב-Deserializer
                            .create();

                    Type type = new TypeToken<Map<String, Set<EffectiveValue>>>(){}.getType();
                    Map<String, Set<EffectiveValue>> distinctValuesMap = gson.fromJson(responseBody, type);

                    Platform.runLater(() -> onSuccess.accept(distinctValuesMap));
                } else {
                    String errorMessage = GSON_INSTANCE.fromJson(responseBody, Map.class).get("error").toString();
                    Platform.runLater(() -> onFailure.accept(errorMessage));
                }
            }
        });
    }

    public void getFilteredSheetDTOFromMultipleCols(Map<String, Set<EffectiveValue>> selectedValues, String rangeDefinition, Consumer<SheetDTO> onSuccess, Consumer<String> onFailure) {
        // המרת המפה של הערכים הנבחרים ל-JSON
        Gson requestGson = new GsonBuilder()
                .registerTypeAdapter(EffectiveValue.class, new EffectiveValueSerializer())  // שימוש ב-Serializer
                .create();
        String jsonBody = requestGson.toJson(selectedValues);

        // יצירת URL עם פרמטר של rangeDefinition
        String finalUrl = HttpUrl.parse(GET_FILTERED_SHEET_DTO)
                .newBuilder()
                //.addQueryParameter("sheetName", sheetName)
                .addQueryParameter("rangeDefinition", rangeDefinition)
                .build()
                .toString();

        // יצירת RequestBody כ-JSON
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

        // יצירת בקשת POST
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        // קריאה אסינכרונית לשרת
        HttpClientUtil.runAsyncByRequest(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> onFailure.accept("Failed to filter data: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Coordinate.class, new CoordinateDeserializer())
                            .registerTypeAdapter(EffectiveValue.class, new EffectiveValueDeserializer())
                            .create();
                    SheetDTO filteredSheet = gson.fromJson(responseBody, SheetDTO.class);

                    //SheetDTO filteredSheet = responseGson.fromJson(responseBody, SheetDTO.class);
                    Platform.runLater(() -> onSuccess.accept(filteredSheet));
                } else {
                    String errorMessage = GSON_INSTANCE.fromJson(responseBody, Map.class).get("error").toString();
                    Platform.runLater(() -> onFailure.accept(errorMessage));
                }
            }
        });
    }
}
