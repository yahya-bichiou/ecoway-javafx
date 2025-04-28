package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Depots;
import services.DepotsServices;

import java.sql.SQLException;
import java.io.IOException;
import java.util.List;

public class DepotFront {


    @FXML
    public HBox depotHBox;

    @FXML
    public void initialize() {
        try {
            DepotsServices ds = new DepotsServices();
            for (Depots depot : ds.select()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/depotPane.fxml"));
                Pane pane = loader.load();
                DepotPane controller = loader.getController();
                controller.setItemData(depot);
                depotHBox.getChildren().add(pane);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void commandePageOpen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CommandeFront.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) depotHBox.getScene().getWindow();
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
