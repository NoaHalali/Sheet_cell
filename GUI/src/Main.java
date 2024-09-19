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
        AppController controller = fxmlLoader.getController();
        controller.setPrimaryStage(primaryStage);

        // קביעת גודל התחלתי לחלון
        Scene scene = new Scene(root, 1500, 700); // שינוי רוחב וגובה התחלתיים
        primaryStage.setScene(scene);

        primaryStage.setTitle("Shticell");

        // הגדרת גודל מינימלי לחלון (למניעת כיווץ יתר של החלון)
//        primaryStage.setMinWidth(700);
//        primaryStage.setMinHeight(500);

        primaryStage.show();

//        FXMLLoader loader = new FXMLLoader(getClass().getResource("CellsTable/table.fxml"));
//        Parent root = loader.load();
//        TableController controller = loader.getController();
//
//        // Initialize the grid with dynamic rows and columns
//        controller.initializeGrid(5, 5); // Example with 4 rows and 4 columns
//
//        primaryStage.setTitle("Dynamic GridPane");
//        primaryStage.setScene(new Scene(root, 600, 400));
//        primaryStage.show();
    }

    public static void main(String[] args) {
        Thread.currentThread().setName("main");
        launch(args);
        //noa
    }
}
