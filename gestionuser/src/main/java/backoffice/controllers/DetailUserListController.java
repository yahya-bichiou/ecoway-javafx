package backoffice.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.user;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class DetailUserListController {

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView profileImageView;

    @FXML
    private Label emailLabel;

    @FXML
    private Label rolesLabel;

    @FXML
    private Label idLabel;

    public void initData(user selectedUser) {
        if (selectedUser != null) {
            idLabel.setText(String.valueOf(selectedUser.getId()));
            nameLabel.setText(selectedUser.getName());
            emailLabel.setText(selectedUser.getEmail());
            rolesLabel.setText(selectedUser.getRoles());

            String imagePath = selectedUser.getImageProfile();
            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    Image image = new Image(imagePath);
                    profileImageView.setImage(image);
                    profileImageView.setClip(new javafx.scene.shape.Circle(
                            profileImageView.getFitWidth() / 2,
                            profileImageView.getFitHeight() / 2,
                            profileImageView.getFitWidth() / 2
                    ));

                } catch (Exception e) {
                    System.out.println("Failed to load profile image: " + e.getMessage());
                }
            }
        }
    }

}
