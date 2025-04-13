package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.userService;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private userService userService = new userService();

    @FXML
    private void handleLoginAction() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in both fields.");
            return;
        }

        if (userService.validateCredentials(email, password)) {
            errorLabel.setText("Login successful!");
            // Proceed to the next screen or dashboard.
        } else {
            errorLabel.setText("Invalid email or password.");
        }
    }
    public void openRegisterPage() {
        try {
            // Load the register.fxml
            Parent registerRoot = FXMLLoader.load(getClass().getResource("/register.fxml"));

            // Get the current stage
            Stage stage = (Stage) emailField.getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(registerRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
