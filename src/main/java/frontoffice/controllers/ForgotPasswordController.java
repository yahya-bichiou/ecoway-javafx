package frontoffice.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.EmailService;
import services.userService;

import java.io.IOException;

public class ForgotPasswordController {

    @FXML
    private TextField emailField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleSendResetLink() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter your email.");
            return;
        }

        userService us = new userService();
        if (us.emailExists(email)) {
            String token = us.generateResetToken(email);
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Reset token generated. Check your email.");

            // Send the reset token via email
            EmailService emailService = new EmailService();
            emailService.sendResetEmail(email, token);

            // After sending email, redirect to Reset Password page
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontoffices/fxml/reset_password.fxml"));
                Parent root = loader.load();

                // Get the controller of ResetPassword page
                ResetPasswordController controller = loader.getController();

                // Set the token into the controller
                controller.setResetToken(token);

                // Open new scene
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Email not found.");
        }
    }



}
