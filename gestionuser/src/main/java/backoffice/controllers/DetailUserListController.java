package backoffice.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.user;

public class DetailUserListController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Label idLabel;

    public void initData(user selectedUser) {
        if (selectedUser != null) {
            idLabel.setText(String.valueOf(selectedUser.getId()));
            nameLabel.setText(selectedUser.getName());
            emailLabel.setText(selectedUser.getEmail());
            roleLabel.setText(selectedUser.getRole());
        }
    }
}
