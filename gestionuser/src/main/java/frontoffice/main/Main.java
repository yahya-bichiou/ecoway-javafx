package frontoffice.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage primaryStage; // Keep a reference to the stage for navigation

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/frontoffices/fxml/fxml/login.fxml")); // Load the Login page by default
        primaryStage.setTitle("JavaFX Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void switchScene(String fxmlFile) throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource(fxmlFile));
        primaryStage.setScene(new Scene(root));
    }



    public static void main(String[] args) {
        launch(args);
    }
}
