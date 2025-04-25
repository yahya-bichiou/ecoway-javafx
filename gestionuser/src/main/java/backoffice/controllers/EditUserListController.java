package backoffice.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.user;
import services.userService;

import java.io.File;

public class EditUserListController {
    private String imagePath;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField profile_pictureField;
    @FXML
    private ImageView profileImageView;

    @FXML
    private TextField rolesField;
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
        rolesField.setText(user.getRoles());

        imagePath = user.getImageProfile(); // Save image path
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image(imagePath);
            profileImageView.setImage(image);
            profileImageView.setClip(new javafx.scene.shape.Circle(
                    profileImageView.getFitWidth() / 2,
                    profileImageView.getFitHeight() / 2,
                    profileImageView.getFitWidth() / 2
            ));
        }
    }


    @FXML
    private void handleSave() {
        if (validateInput()) {
            currentUser.setName(nameField.getText());
            currentUser.setEmail(emailField.getText());
            currentUser.setPassword(passwordField.getText());
            currentUser.setRoles(rolesField.getText());
            currentUser.setImageProfile(imagePath); // Save updated image path

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
        if (rolesField.getText() == null || rolesField.getText().isEmpty()) {
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

    @FXML
    private void handleChangeImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(profileImageView.getScene().getWindow());
        if (selectedFile != null) {
            imagePath = selectedFile.toURI().toString(); // Save selected image path

            Image image = new Image(imagePath);
            profileImageView.setImage(image);
            profileImageView.setClip(new javafx.scene.shape.Circle(
                    profileImageView.getFitWidth() / 2,
                    profileImageView.getFitHeight() / 2,
                    profileImageView.getFitWidth() / 2
            ));
        }
    }

}