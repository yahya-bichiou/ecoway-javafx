package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class FrontTamplateController {



    @FXML
        private void NavigateToEvent(ActionEvent event) { // Add ActionEvent parameter
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/listEventFront.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Consider showing an alert to the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Navigation Error");
                alert.setHeaderText("Failed to load page");
                alert.setContentText("Could not load the requested page.");
                alert.showAndWait();
            }
        }
    }