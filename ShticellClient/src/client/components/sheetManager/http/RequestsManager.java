package client.components.sheetManager.http;

import client.components.Utils.Constants;
import client.components.Utils.StageUtils;
import client.components.Utils.http.HttpClientUtil;
import javafx.application.Platform;
import okhttp3.*;
import parts.SheetDTO;

import java.io.IOException;

import static client.components.Utils.Constants.GET_SHEET_DTO;


public class RequestsManager {

    public SheetDTO getSheetDTO(String sheetName) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        String finalUrl = HttpUrl
                .parse(GET_SHEET_DTO)
                .newBuilder()
                .addQueryParameter("sheetName", sheetName)
                .build()
                .toString();

        System.out.println("New request is launched for: " + GET_SHEET_DTO );

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // טיפול בשגיאה במקרה של כשל
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.isSuccessful()) {
                    Platform.runLater(() -> {
                        // טיפול במקרה שהבקשה הצליחה
                        System.out.println("Upload successful: " + responseBody);
                        return null;
                    });

                } else {
                    Platform.runLater(() ->
                            StageUtils.showAlert("Error", "Error uploading file" + responseBody)
                    );
                }
            }
        });
    }
}
