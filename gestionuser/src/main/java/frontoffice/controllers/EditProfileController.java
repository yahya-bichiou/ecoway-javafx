package frontoffice.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.user;
import services.userService;

import java.io.File;
import java.io.IOException;

public class EditProfileController {

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ImageView profileImageView;

    @FXML
    private Label statusLabel;

    private File selectedImageFile;

    private final userService userService = new userService();

    @FXML
    public void initialize() {
        user currentUser = UserSession.getInstance().getUser();
        nameField.setText(currentUser.getName());

        if (currentUser.getImageProfile() != null && !currentUser.getImageProfile().isEmpty()) {
            profileImageView.setImage(new Image("file:" + currentUser.getImageProfile()));
        } else {
            profileImageView.setImage(new Image(getClass().getResource("/assets/user.png").toExternalForm()));
        }
    }

    @FXML
    private void handleChangePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(nameField.getScene().getWindow());
        if (file != null) {
            selectedImageFile = file;
            profileImageView.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    private void handleSave() {
        user currentUser = UserSession.getInstance().getUser();
        String newName = nameField.getText().trim();
        String newPassword = passwordField.getText().trim();

        if (!newName.isEmpty()) {
            currentUser.setName(newName);
        }

        if (!newPassword.isEmpty()) {
            currentUser.setPassword(newPassword); // You can hash if needed
        }

        if (selectedImageFile != null) {
            currentUser.setImageProfile(selectedImageFile.getAbsolutePath());
        }

        // Save updated user in DB
        userService.update(currentUser);

        // Update session
        UserSession.getInstance().setUser(currentUser);

        goToProfile();
    }

    @FXML
    private void handleCancel() {
        goToProfile();
    }

    private void goToProfile() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/frontoffices/fxml/profile.fxml"));
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
