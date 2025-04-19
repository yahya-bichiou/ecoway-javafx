package tn.esprit;

import controllers.DashboardController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage; // Keep a reference to the stage for navigation

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        //Parent root = FXMLLoader.load(getClass().getResource("/listEventBack.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml")); // Load the Login page by default
        primaryStage.setTitle("JavaFX Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void switchScene(String fxmlFile) throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource(fxmlFile));
        primaryStage.setScene(new Scene(root));
    }

    public static void switchToDashboard(String userName) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/dashboard.fxml"));
        Parent root = loader.load();

        DashboardController controller = loader.getController();
        controller.setWelcomeMessage("Welcome, " + userName);

        primaryStage.setScene(new Scene(root));
    }


    public static void main(String[] args) {
        launch(args);
    }

}