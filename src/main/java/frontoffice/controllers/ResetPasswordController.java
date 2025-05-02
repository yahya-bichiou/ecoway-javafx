package frontoffice.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.userService;

import java.io.IOException;

public class ResetPasswordController {
    @FXML
    private TextField tokenField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private Label messageLabel;

    private String resetToken;

    @FXML
    public void initialize() {
        tokenField.setVisible(false);
    }



    @FXML
    private void handleResetPassword() {
        String token = tokenField.getText().trim();
        String newPassword = newPasswordField.getText();

        if (token.isEmpty() || newPassword.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter both fields.");
            return;
        }

        // Show confirmation dialog
        boolean confirmed = showConfirmationDialog("Confirm Password Reset", "Are you sure you want to reset your password?");

        if (confirmed) {
            userService us = new userService();
            boolean success = us.resetPassword(token, newPassword);

            if (success) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Password reset successfully.");
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Invalid or expired token.");
            }
        } else {
            messageLabel.setStyle("-fx-text-fill: orange;");
            messageLabel.setText("Password reset canceled.");
        }
    }

    private boolean showConfirmationDialog(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);


        java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK;
    }




    public void setResetToken(String token) {
        this.resetToken = token;
        tokenField.setText(token); // Set it automatically
    }


    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            // Load the login screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontoffices/fxml/login.fxml"));
            Parent loginScreen = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene to the login screen
            Scene scene = new Scene(loginScreen);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle any exceptions that occur while loading the login screen
        }
    }



}
