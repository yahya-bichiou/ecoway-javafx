package frontoffice.controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.userService;
import services.CaptchaService;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

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
    private void handleRegisterAction(ActionEvent event) {
        resetError();

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String roles = "[\"ROLE_USER\"]";

        if (!validateInputs(name, email, password, confirmPassword)) {
            return;
        }

        // CAPTCHA check
        if (!showCaptchaPopup()) {
            showError("CAPTCHA verification failed.");
            return;
        }

        // Register
        if (userService.addUser(name, email, password, "", roles)) {
            showError("Registration successful!");
            navigateToLogin();
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
            stage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load the login page. Please try again.");
        }
    }

    private boolean showCaptchaPopup() {
        Map<String, String> captchaData = CaptchaService.generateCaptcha();

        String captchaId = captchaData.get("captchaId");
        String base64Image = captchaData.get("base64Image");

        if (base64Image == null || base64Image.isEmpty()) {
            showError("Failed to generate CAPTCHA.");
            return false;
        }

        // Decode base64 to JavaFX Image
        String base64 = base64Image.replace("data:image/png;base64,", "");
        byte[] imageBytes = Base64.getDecoder().decode(base64);
        Image image = new Image(new java.io.ByteArrayInputStream(imageBytes));

        // UI Elements
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(80);
        imageView.setPreserveRatio(true);

        TextField inputField = new TextField();
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        VBox layout = new VBox(10, new Label("Please enter the CAPTCHA below:"), imageView, inputField, errorLabel);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("CAPTCHA Verification");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(layout);

        // Validate CAPTCHA before allowing OK
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            String userInput = inputField.getText();
            boolean valid = CaptchaService.validateCaptcha(captchaId, userInput);
            if (!valid) {
                errorLabel.setText("Incorrect CAPTCHA. Try again.");
                event.consume(); // prevent dialog from closing
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }


}
