package frontoffice.controllers;

import com.google.api.services.oauth2.model.Userinfo;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.user;
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

        if (userService.validateCredentials(email, password)) {
            user loggedInUser = userService.getUserByEmail(email);
            UserSession.initSession(loggedInUser);


            if (loggedInUser.getRoles().contains("ROLE_ADMIN")) {
                navigateTo("/backofice/fxml/userList.fxml"); // Back office dashboard
            } else {
                navigateTo("/frontoffices/fxml/profile.fxml"); // Front office profile
            }

        } else {
            showError("Incorrect email or password.");
            shakeField(emailField);
        }
    }
    private void navigateTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load the page.");
        }
    }



    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            shakeField(emailField); // Highlight the email field
            showError("Email is required.");
            return false;
        }

        if (!email.matches("^[\\w-\\.]+@[\\w-\\.]+\\.\\w{2,4}$")) {
            shakeField(emailField);
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
            Parent root = FXMLLoader.load(getClass().getResource("/frontoffices/fxml/register.fxml"));
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

    @FXML
    private void handleGoogleLogin() {
        try {
            Userinfo userInfo = services.GoogleLoginService.loginWithGoogle();

            String email = userInfo.getEmail();
            String name = userInfo.getName();
            String profilePic = userInfo.getPicture();

            user existingUser = userService.getUserByEmail(email);

            if (existingUser == null) {

                String defaultPassword = "google";
                String defaultRole = "[\"ROLE_USER\"]";

                userService.addUser(name, email, defaultPassword, profilePic, defaultRole);
                existingUser = userService.getUserByEmail(email); // Fetch again
            }

            // Initialize session and redirect
            UserSession.initSession(existingUser);

            if (existingUser.getRoles().contains("ROLE_ADMIN")) {
                navigateTo("/backofice/fxml/userList.fxml");
            } else {
                navigateTo("/frontoffices/fxml/profile.fxml");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showError("Google login failed.");
        }
    }

    @FXML
    private void handleSendResetLink() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/frontoffices/fxml/ForgotPassword.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load forgot password page.");
        }
    }

}
