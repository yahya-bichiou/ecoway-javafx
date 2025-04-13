package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.userService;

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField imageProfileField;

    @FXML
    private Label errorLabel;

    private userService userService = new userService();

    @FXML
    private void handleRegisterAction() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String imageProfile = imageProfileField.getText();

        // Input validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Please fill in all required fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }

        if (userService.emailExists(email)) {
            errorLabel.setText("Email is already registered.");
            return;
        }

        // Register the user
        if (userService.addUser(name, email, password, imageProfile)) {
            errorLabel.setText("Registration successful!");
        } else {
            errorLabel.setText("Registration failed. Please try again.");
        }
    }
}
