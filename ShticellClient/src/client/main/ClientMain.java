package client.main;

import client.components.Utils.http.HttpClientUtil;
import client.components.login.LoginController;
import client.components.mainAppController.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static client.components.Utils.Constants.LOGIN_PAGE_FXML_RESOURCE_LOCATION;
import static client.components.Utils.Constants.MAIN_PAGE_FXML_RESOURCE_LOCATION;

public class ClientMain extends Application {

    private AppController mainController;
    private LoginController loginController;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle("Chat App Client");

        //URL loginPage = getClass().getResource(MAIN_PAGE_FXML_RESOURCE_LOCATION);
        URL loginPage = getClass().getResource(MAIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPage);
            Parent root = fxmlLoader.load();
            mainController = fxmlLoader.getController();

            Scene scene = new Scene(root, 700, 600);
            primaryStage.setScene(scene);
           // mainController.setScene(scene);  // העברת הסצנה ל-AppController לשימוש ב-SkinSelector
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() throws Exception {
//        HttpClientUtil.shutdown();
//        appController.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
