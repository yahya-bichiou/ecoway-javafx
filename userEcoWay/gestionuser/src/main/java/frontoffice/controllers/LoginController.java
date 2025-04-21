package frontoffice.controllers;

import backoffice.controllers.UserListController;
import controllers.FrontTamplateController;
import controllers.listEventBackController;
import controllers.listEventFrontController;
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

    //teba3a evente ya 8ali  :
    @FXML
    private void handleLoginAction() {
        resetError();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (!validateInputs(email, password)) {
            return;
        }

        if (userService.validateCredentials(email, password)) {
            user loggedInUser = userService.getUserByEmail(email);
            navigateToProfilePage(loggedInUser);
        } else {
            showError("Incorrect email or password.");
            shakeField(emailField);
        }
    }

    // ne5ou el name ou el email
    private void navigateToProfilePage(user loggedInUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontTamplate.fxml"));
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/listEventBack.fxml"));
            Parent root = loader.load();

            FrontTamplateController controller = loader.getController();
            controller.setLoggedInUser(loggedInUser);

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Back Office - User Management");
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load the user management page. Please try again.");
        }
    }
    /*@FXML
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
        } else {
            showError("Incorrect email or password.");
            shakeField(emailField); // Shake the email field to indicate an error
        }
    }

    private void navigateToProfilePage(String userName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listEventFront.fxml"));
            Parent root = loader.load();


            // Switch to User List Page
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Back Office - User Management");
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load the user management page. Please try again.");
        }
    }*/

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
