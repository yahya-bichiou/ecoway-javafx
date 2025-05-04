package controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Home extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage)  {
        try {
<<<<<<< HEAD
            Parent root = FXMLLoader.load(getClass().getResource("/DepotsFront.fxml"));
=======
            Parent root = FXMLLoader.load(getClass().getResource("/CommandesBack.fxml"));
>>>>>>> origin/commande-livraison
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();


        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}