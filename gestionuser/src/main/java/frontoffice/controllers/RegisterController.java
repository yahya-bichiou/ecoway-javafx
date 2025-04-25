package frontoffice.controllers;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.userService;

import java.io.IOException;

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
    private Label errorLabel;

    private final userService userService = new userService();

    @FXML
    private void handleRegisterAction() {
        resetError(); // Clear any previous errors

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String roles = "[\"ROLE_USER\"]";

        // Validate inputs
        if (!validateInputs(name, email, password, confirmPassword)) {
            return;
        }

        // Process registration if validation passes
        if (userService.addUser(name, email, password, "", roles)) {
            showError("Registration successful!");
            navigateToLogin(); // Navigate to the login page
        } else {
            showError("Registration failed. Please try again.");
        }
    }

    private boolean validateInputs(String name, String email, String password, String confirmPassword) {
        if (name.isEmpty()) {
            shakeField(nameField);
            showError("Name is required.");
            return false;
        }

        if (email.isEmpty()) {
            shakeField(emailField);
            showError("Email is required.");
            return false;
        }

        if (!email.matches("^[\\w-\\.]+@[\\w-\\.]+\\.\\w{2,4}$")) {
            shakeField(emailField);
            showError("Invalid email format.");
            return false;
        }

        if (password.isEmpty()) {
            shakeField(passwordField);
            showError("Password is required.");
            return false;
        }

        if (password.length() < 8 || !password.matches(".*\\d.*") || !password.matches(".*[a-zA-Z].*")) {
            shakeField(passwordField);
            showError("Password must be at least 8 characters long and contain letters and numbers.");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            shakeField(confirmPasswordField);
            showError("Passwords do not match.");
            return false;
        }

        if (userService.emailExists(email)) {
            shakeField(emailField);
            showError("This email is already registered.");
            return false;
        }

        return true;
    }

    private void resetError() {
        errorLabel.setText("");
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }

    private void shakeField(Control field) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(50), field);
        transition.setFromX(0);
        transition.setByX(10);
        transition.setCycleCount(6);
        transition.setAutoReverse(true);
        transition.play();
    }

    public void navigateToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/frontoffices/fxml/login.fxml"));
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load the login page. Please try again.");
        }
    }


}
