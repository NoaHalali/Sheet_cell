package client.components.mainAppController;

import client.components.login.LoginController;
import client.components.multiSheetsScreen.MultiSheetsScreenController;
import client.components.sheetManager.SheetManagerController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import shticell.permissions.PermissionType;

import java.io.IOException;
import java.net.URL;

import static client.components.Utils.Constants.*;

public class AppController {


    private GridPane loginComponent;
    private LoginController logicController;

    //private Parent chatRoomComponent;
    private ScrollPane sheetManagerComponent;
    private SheetManagerController sheetManagerController;
    
    private BorderPane multiSheetsScreenComponent;
    private MultiSheetsScreenController multiSheetsScreenController;

    @FXML private Label userGreetingLabel;
    @FXML private AnchorPane mainPanel;

    private final StringProperty currentUserName;
    private Stage stage;

    public AppController() {
        currentUserName = new SimpleStringProperty(JHON_DOE);
    }

    @FXML
    public void initialize() {
        userGreetingLabel.textProperty().bind(Bindings.concat("Hello ", currentUserName));

        // prepare components
        loadLoginPage();
        loadSheetManagerPage();
        loadSheetAndPermissionManager();
    }

    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }

    private void setMainPanelTo(Parent pane) {
        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(pane);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
    }

//    @Override
//    public void close() throws IOException {
//        chatRoomComponentController.close();
//    }
    private void loadSheetAndPermissionManager() {
        URL multiSheetsScreenURL = getClass().getResource(MULTI_SHEETS_SCREEN_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(multiSheetsScreenURL);
            multiSheetsScreenComponent = fxmlLoader.load();
            multiSheetsScreenController = fxmlLoader.getController();
            multiSheetsScreenController.setMainController(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            logicController = fxmlLoader.getController();
            logicController.setChatAppMainController(this);

            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSheetManagerPage() {
        URL loginPageUrl = getClass().getResource(SHEET_MANAGER_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            sheetManagerComponent = fxmlLoader.load();
            sheetManagerController = fxmlLoader.getController();
            sheetManagerController.setMainController(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void updateHttpLine(String line) {
//        httpStatusComponentController.addHttpStatusLine(line);
//    }

    public void switchToSheetManager(String sheetName, ObjectProperty<PermissionType> permissionTypeProperty) {
        Platform.runLater(() -> {
            //sheetManagerController.setActive();
            setMainPanelTo(sheetManagerComponent);
            sheetManagerController.initializeComponentsAfterLoad(sheetName, permissionTypeProperty);
        });
    }

    public void switchToMultiSheetsScreen() {

        setMainPanelTo(multiSheetsScreenComponent);
        multiSheetsScreenController.setActive();

    }

//    public void switchToLogin() {
//        Platform.runLater(() -> {
//            currentUserName.set(JHON_DOE);
//           // chatRoomComponentController.setInActive();
//            setMainPanelTo(loginComponent);
//        });
//    }


    public void setPrimaryStage(Stage primaryStage) {
        this.stage = primaryStage;
//        sheetsAndPermissionsManagerController.setPrimaryStage(primaryStage);
//        sheetManagerController.setPrimaryStage(primaryStage);
    }
}
