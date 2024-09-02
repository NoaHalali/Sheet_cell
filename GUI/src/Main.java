import CellsTable.TableController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("C:\\Users\\amir\\IdeaProjects\\Sheet_cell\\GUI\\src\\MainComponent\\app.fxml"));
//        HBox root = loader.load();
//        Scene scene = new Scene(root, 600, 400); // Adjust size as necessary
//
//        primaryStage.setTitle("JavaFX App with Scene Builder");
//        primaryStage.setScene(scene);
//        primaryStage.show();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/MainComponent/app.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        Scene scene = new Scene(root, 500, 550);
        primaryStage.setScene(scene);
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
