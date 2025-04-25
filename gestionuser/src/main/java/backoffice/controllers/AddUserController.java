package backoffice.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.user;
import services.userService;

public class AddUserController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField profile_pictureField;

    @FXML
    private ComboBox<String> rolesComboBox;

    @FXML
    private Button submitButton;

    @FXML
    void handleAddUser() {
        try {
            // Validate user input
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String profile_picture = profile_pictureField.getText();
            String roles = rolesComboBox.getValue();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || roles == null) {
                showAlert("Erreur", "Tous les champs sont obligatoires.");
                return;
            }

            // Call userService to save the user
            user newUser = new user(name, email, password, profile_picture, roles);
            userService service = new userService();
            service.add(newUser); // Using the existing 'add' method

            showAlert("Succès", "Utilisateur ajouté avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ajouter l'utilisateur.");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void addUser(ActionEvent actionEvent) {
        try {
            // Validate user input
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String profile_picture = profile_pictureField.getText();
            String roles = rolesComboBox.getValue();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || roles == null) {
                showAlert("Erreur", "Tous les champs sont obligatoires.");
                return;
            }

            // Call userService to save the user
            user newUser = new user(name, email, password, profile_picture, roles);
            userService service = new userService();
            service.add(newUser); // Using the existing 'add' method

            showAlert("Succès", "Utilisateur ajouté avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ajouter l'utilisateur.");
        }
    }
}
