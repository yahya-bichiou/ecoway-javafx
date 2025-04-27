package org.example.utile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/listeProduitBack.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/produitfront.fxml"));
         //Parent root = FXMLLoader.load(getClass().getResource("/AddproduitBack.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Gestion des produits");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
