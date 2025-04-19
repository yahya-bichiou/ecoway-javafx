package frontoffice.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileController {

    @FXML
    private Label welcomeMessage;

    public void setWelcomeMessage(String userName) {
        welcomeMessage.setText("Welcome, " + userName + "!");
    }

    @FXML
    private void handleLogoutAction() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/frontoffices/fxml/fxml/login.fxml")); // Load the login page
            Stage stage = (Stage) welcomeMessage.getScene().getWindow(); // Get the current stage
            stage.setScene(new Scene(root)); // Set the login scene on the current stage
            stage.show(); // Display the updated scene
            System.out.println("User logged out.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
