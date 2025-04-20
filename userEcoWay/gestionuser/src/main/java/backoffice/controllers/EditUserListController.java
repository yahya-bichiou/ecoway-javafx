package backoffice.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.user;
import services.userService;

public class EditUserListController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField imageProfileField;
    @FXML
    private TextField roleField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private user currentUser;

    public void setUserData(user user) {
        this.currentUser = user;
        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        passwordField.setText(user.getPassword());
        imageProfileField.setText(user.getImageProfile());
        roleField.setText(user.getRole());
    }

    @FXML
    private void handleSave() {
        if (validateInput()) {
            currentUser.setName(nameField.getText());
            currentUser.setEmail(emailField.getText());
            currentUser.setPassword(passwordField.getText());
            currentUser.setImageProfile(imageProfileField.getText());
            currentUser.setRole(roleField.getText());

            userService service = new userService();
            service.update(currentUser);

            // Close the window
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private boolean validateInput() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().isEmpty()) {
            errorMessage += "Nom invalide!\n";
        }
        if (emailField.getText() == null || emailField.getText().isEmpty()) {
            errorMessage += "Email invalide!\n";
        }
        if (passwordField.getText() == null || passwordField.getText().isEmpty()) {
            errorMessage += "Mot de passe invalide!\n";
        }
        if (roleField.getText() == null || roleField.getText().isEmpty()) {
            errorMessage += "Rôle invalide!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert("Erreur de validation",
                    "Veuillez corriger les erreurs suivantes",
                    errorMessage);
            return false;
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}