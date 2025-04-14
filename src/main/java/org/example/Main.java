package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/listEventBack.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/listEventFront.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/AddEventBack.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Gestion des Evenements et Participnts");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
