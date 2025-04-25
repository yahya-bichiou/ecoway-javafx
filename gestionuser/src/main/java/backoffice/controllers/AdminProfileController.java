package backoffice.controllers;

import frontoffice.controllers.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.user;

import java.io.IOException;

public class AdminProfileController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private ImageView profileImageView;

    @FXML
    private Button editProfileButton;

    @FXML
    private Button backButton;


    @FXML
    public void initialize() {
        user currentUser = UserSession.getInstance().getUser();

        nameLabel.setText(currentUser.getName());
        emailLabel.setText(currentUser.getEmail());
        roleLabel.setText(currentUser.getRoles());

        String profilePic = currentUser.getImageProfile();
        if (profilePic != null && !profilePic.isEmpty()) {
            profileImageView.setImage(new Image("file:" + profilePic));
        }
    }

    @FXML
    private void handleEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/backofice/fxml/EditAdminProfile.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Edit Profile");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleGoBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/backofice/fxml/userList.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("User Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
