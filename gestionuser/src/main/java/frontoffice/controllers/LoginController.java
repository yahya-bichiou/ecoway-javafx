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

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final userService userService = new userService();

    @FXML
    public void initialize() {
        System.out.println("Error Label Initialized: " + errorLabel);
    }

    @FXML
    private void handleLoginAction() {
        resetError(); // Clear previous errors

        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (!validateInputs(email, password)) {
            return; // Stop further processing if validation fails
        }

        // Process login if validation passes
        if (userService.validateCredentials(email, password)) {
            navigateToProfilePage(email); // Pass the user's email or name to the profile page
            showError("Incorrect email or password.");
        }else {
            showError("Incorrect email or password.");
            shakeField(emailField); // Shake the email field to indicate an error
        }
    }

    private void navigateToProfilePage(String userName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontoffices/fxml/fxml/profile.fxml"));
            Parent root = loader.load();

            // Pass user data to ProfileController
            ProfileController profileController = loader.getController();
            profileController.setWelcomeMessage(userName);

            // Switch to Profile Page
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load the profile page. Please try again.");
        }
    }


    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            shakeField(emailField); // Highlight the email field
            showError("Email is required.");
            return false;
        }

        if (!email.matches("^[\\w-\\.]+@[\\w-\\.]+\\.\\w{2,4}$")) {
            shakeField(emailField); // Indicate invalid email format
            showError("Invalid email format.");
            return false;
        }

        if (password.isEmpty()) {
            shakeField(passwordField); // Highlight the password field
            showError("Password is required.");
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


    @FXML
    private void openRegisterPage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/frontoffices/fxml/fxml/register.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load the register page. Please try again.");
        }
    }
    private void shakeField(Control field) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(50), field);
        transition.setFromX(0);
        transition.setByX(10);
        transition.setCycleCount(6);
        transition.setAutoReverse(true);
        transition.play();
    }
}
