package main;
import components.MainComponent.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/components/MainComponent/app.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        // קבלת ה-AppController מה-FXMLLoader
        AppController controller = fxmlLoader.getController();
        controller.setPrimaryStage(primaryStage); // העברת ה-Stage ל-AppController

        // יצירת סצנה
        Scene scene = new Scene(root, 1500, 700);

        // הוספת קובץ ה-CSS
        //scene.getStylesheets().add(getClass().getResource("/main/main.css").toExternalForm()); // כאן מוסיפים את ה-CSS

        primaryStage.setScene(scene);
        controller.setScene(scene);  // העברת הסצנה ל-AppController לשימוש ב-SkinSelector

        primaryStage.setTitle("Shticell");
        primaryStage.show();
    }


    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        launch(args);
    }
}
