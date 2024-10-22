package client.components.sheetManager.parts.top.updates;

import client.components.Utils.http.HttpClientUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;


import java.io.IOException;
import java.util.TimerTask;
import java.util.function.Consumer;

import static client.components.Utils.Constants.*;

public class SheetUpdatesRefresher extends TimerTask  {

    private final Consumer<String> messageConsumer;
    private final String finalUrl;

    public SheetUpdatesRefresher(Consumer<String> messageConsumer, String sheetName) {
        this.messageConsumer = messageConsumer;
        this.finalUrl = HttpUrl
                .parse(CHECK_SHEET_VERSION)  // כתובת ה-URL של הסרבלט
                .newBuilder()
                .addQueryParameter("sheetName", sheetName)  // שם הגיליון
                .build()
                .toString();
    }

    @Override
    public void run() {

        // בדיקת עדכונים לגרסת הגיליון עם HttpClientUtil
        HttpClientUtil.runAsyncByUrl(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // טיפול בכשלון של הבקשה (ניתן להוסיף לוג או הודעה למשתמש)
                messageConsumer.accept("Error checking for updates.");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                //String jsonResponse = response.body().string();

                // בדיקה אם הגרסה אינה מעודכנת לפי קוד סטטוס
                if(response.code() == 200) {
                    // יש עדכון - מקבלים את ההודעה על גרסה מעודכנת
                    String message = "A new version of the sheet is available. Please refresh.";
                    messageConsumer.accept(message); // העברת ההודעה למשתמש
                } else if (response.code() == 204) {
                    // אין עדכון - ממשיכים כרגיל
                    messageConsumer.accept(""); // אין צורך בעדכון
                } else {
                    // טיפול במקרה של שגיאות אחרות
                    messageConsumer.accept("Error: Unable to check for sheet version updates.");
                }
            }
        });
    }

//    public String getUpdateMessageFromPermissionUpdate(PermissionUpdate permissionUpdate) {
//
//        String message ="Update: permission: " + permissionUpdate.getPermission() +", for sheet: " + permissionUpdate.getSheetName()
//                + ", has been changed to: " + permissionUpdate.getRequestStatus();
//        return message;
//    }
}
