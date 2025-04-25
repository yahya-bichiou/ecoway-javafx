package backoffice.controllers;

import frontoffice.controllers.UserSession;
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

import java.io.File;
import java.io.IOException;

public class EditAdminProfileController {

    @FXML private TextField nameField;
    @FXML private PasswordField passwordField;
    @FXML private ImageView previewImage;
    @FXML private Button choosePicBtn;
    @FXML private Button saveBtn;

    private File selectedImage;

    @FXML
    public void initialize() {
        user currentUser = UserSession.getInstance().getUser();
        if (currentUser != null) {
            nameField.setText(currentUser.getName());
            previewImage.setImage(new Image("file:" + currentUser.getImageProfile()));
        }
    }

    @FXML
    private void chooseProfilePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        selectedImage = fileChooser.showOpenDialog(null);
        if (selectedImage != null) {
            previewImage.setImage(new Image("file:" + selectedImage.getAbsolutePath()));
        }
    }

    public void initData(user currentUser) {
        if (currentUser != null) {
            nameField.setText(currentUser.getName());
            previewImage.setImage(new Image("file:" + currentUser.getImageProfile()));
            // If needed, you can store the current user for further editing
        }
    }
    @FXML
    private void cancelChanges() {
        goToUserList();
    }

    @FXML
    private void saveChanges() {
        user updatedUser = UserSession.getInstance().getUser();
        updatedUser.setName(nameField.getText());

        if (!passwordField.getText().isEmpty()) {
            updatedUser.setPassword(passwordField.getText()); // Ensure hashing is done elsewhere
        }

        if (selectedImage != null) {
            updatedUser.setImageProfile(selectedImage.getAbsolutePath());
        }

        UserSession.getInstance().setUser(updatedUser);

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Profile updated successfully!", ButtonType.OK);
        alert.showAndWait();

        goToUserList();
    }
    private void goToUserList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/backofice/fxml/userList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) saveBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("User Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
