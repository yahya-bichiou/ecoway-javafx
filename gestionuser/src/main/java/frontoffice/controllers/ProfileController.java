package frontoffice.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.user;
import services.userService;


import java.io.IOException;

public class ProfileController {

    @FXML
    private Label welcomeMessage;

    @FXML
    private ImageView profileIcon;


    @FXML
    private ImageView profileImageView;



    @FXML
    private Label userNameLabel;

    public void setWelcomeMessage(String userName) {
        welcomeMessage.setText("Welcome, " + userName + "!");
    }


    @FXML
    public void initialize() {
        userService userService = new userService();
        String email = UserSession.getInstance().getUser().getEmail();
        user currentUser = userService.getUserByEmail(email);

        String name = currentUser.getName();
        String imagePath = currentUser.getImageProfile();

        welcomeMessage.setText("Welcome, " + name + "!");
        userNameLabel.setText(name);

        Image profileImage;
        if (imagePath != null && !imagePath.isEmpty()) {
            profileImage = new Image(imagePath);
        } else {
            profileImage = new Image(getClass().getResource("/assets/user.png").toExternalForm());
        }

        profileIcon.setImage(profileImage);
        profileImageView.setImage(profileImage);

        // Apply circular clips once layout is complete
        profileIcon.imageProperty().addListener((obs, oldVal, newVal) -> applyCircularClip(profileIcon));
        profileImageView.imageProperty().addListener((obs, oldVal, newVal) -> applyCircularClip(profileImageView));
    }



    private void applyCircularClip(ImageView imageView) {
        imageView.setClip(null); // Clear existing clip if any
        double radius = Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2;
        javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(
                imageView.getFitWidth() / 2,
                imageView.getFitHeight() / 2,
                radius
        );
        imageView.setClip(circle);
    }






    @FXML
    private void goToEditProfile() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/frontoffices/fxml/editProfile.fxml"));
            Stage stage = (Stage) welcomeMessage.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @FXML
    private void goToProfile() {
        navigateTo("/frontoffices/fxml/profile.fxml");
    }

    private void navigateTo(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) welcomeMessage.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleLogoutAction() {
        try {
            UserSession.clearSession(); // <== clear session on logout
            Parent root = FXMLLoader.load(getClass().getResource("/frontoffices/fxml/login.fxml"));
            Stage stage = (Stage) welcomeMessage.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            System.out.println("User logged out.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
