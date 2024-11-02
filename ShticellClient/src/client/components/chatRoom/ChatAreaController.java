package client.components.chatRoom;


import client.components.Utils.Constants;
import client.components.Utils.StageUtils;
import client.components.Utils.http.HttpClientUtil;
import client.components.chatRoom.model.ChatLinesWithVersion;
import client.components.mainAppController.AppController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.util.Timer;
import java.util.stream.Collectors;

import static client.components.Utils.Constants.CHAT_LINE_FORMATTING;
import static client.components.Utils.Constants.REFRESH_RATE;


public class ChatAreaController implements Closeable {

    public Button backToDashboardButton;
    private AppController mainController;

    private final IntegerProperty chatVersion;
    private final BooleanProperty autoScroll;
    private final BooleanProperty autoUpdate;
    private ChatAreaRefresher chatAreaRefresher;
    private Timer timer;

    @FXML private ToggleButton autoScrollButton;
    @FXML private TextArea chatLineTextArea;
    @FXML private TextArea mainChatLinesTextArea;
    @FXML private Label chatVersionLabel;

    public ChatAreaController() {
        chatVersion = new SimpleIntegerProperty();
        autoScroll = new SimpleBooleanProperty();
        autoUpdate = new SimpleBooleanProperty();
    }

    @FXML
    public void initialize() {
        autoScroll.bind(autoScrollButton.selectedProperty());
        chatVersionLabel.textProperty().bind(Bindings.concat("Chat Version: ", chatVersion.asString()));
    }

    public BooleanProperty autoUpdatesProperty() {
        return autoUpdate;
    }

    @FXML
    void sendButtonClicked(ActionEvent event) {
        String chatLine = chatLineTextArea.getText();
        String finalUrl = HttpUrl
                .parse(Constants.SEND_CHAT_LINE)
                .newBuilder()
                .addQueryParameter("userstring", chatLine)
                .build()
                .toString();

        HttpClientUtil.runAsyncByUrl(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                StageUtils.showAlert("Error" ,"Failed to send message");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    StageUtils.showAlert("Error" ,"Attempt to send chat line [" + chatLine + "] request ended with failure. Error code: " + response.code());
                }
            }
        });

        chatLineTextArea.clear();
    }



    private void updateChatLines(ChatLinesWithVersion chatLinesWithVersion) {
        if (chatLinesWithVersion.getVersion() != chatVersion.get()) {
            String deltaChatLines = chatLinesWithVersion
                    .getEntries()
                    .stream()
                    .map(singleChatLine -> {
                        long time = singleChatLine.getTime();
                        return String.format(CHAT_LINE_FORMATTING, time, time, time, singleChatLine.getUsername(), singleChatLine.getChatString());
                    }).collect(Collectors.joining());

            Platform.runLater(() -> {
                chatVersion.set(chatLinesWithVersion.getVersion());

                if (autoScroll.get()) {
                    mainChatLinesTextArea.appendText(deltaChatLines);
                    mainChatLinesTextArea.selectPositionCaret(mainChatLinesTextArea.getLength());
                    mainChatLinesTextArea.deselect();
                } else {
                    int originalCaretPosition = mainChatLinesTextArea.getCaretPosition();
                    mainChatLinesTextArea.appendText(deltaChatLines);
                    mainChatLinesTextArea.positionCaret(originalCaretPosition);
                }
            });
        }
    }

    public void startListRefresher() {
        chatAreaRefresher = new ChatAreaRefresher(
                chatVersion,
                autoUpdate,
                this::updateChatLines,errormessage->{
                    StageUtils.showAlert("Error" ,errormessage);
        });
        timer = new Timer();
        timer.schedule(chatAreaRefresher, REFRESH_RATE, REFRESH_RATE);
    }
    @FXML
    public void returnToDashboard() throws IOException{
        chatAreaRefresher.cancel();
        mainController.switchToMultiSheetsScreen();
    }

    @Override
    public void close() throws IOException {
        chatVersion.set(0);
        chatLineTextArea.clear();
        if (chatAreaRefresher != null && timer != null) {
            chatAreaRefresher.cancel();
            timer.cancel();
        }
    }

    public void setMainController(AppController appController) {
        mainController=appController;
    }
}